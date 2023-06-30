package com.example.TransportCompany.services;

import com.example.TransportCompany.Mongo.InvoiceMongoDao;
import com.example.TransportCompany.model.*;
import com.example.TransportCompany.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
 class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    CourseService courseService;
    @Autowired
    ClientService clientService;
    @Autowired

    CompanyRepository companyRepository;
    @Autowired
    InvoiceMongoDao invoiceMongoDao;
    private final Calendar calendar = Calendar.getInstance();
    @Value("${per_km_rate}")
    private BigDecimal kilometerRate;

    @Override
    public Invoice addInvoice(Invoice invoice, int courseId) {
        Course courseEntity = courseService.findCourse(courseId);
       Optional<Client> clientEntity=clientService.getClient(courseEntity.getClientsId().getClientId());
            if (Objects.equals(courseEntity.getType().getName(), "CLOSED")&& clientEntity.isPresent()) {
                invoice.setCourseId(courseEntity.getCourseId());
                invoice.setClientId(String.valueOf(courseEntity.getClientsId().getClientId()));
                invoice.setClientEmail(String.valueOf(clientEntity.get().getEmail()));
                invoice.setDateOfService(courseEntity.getUpdatedAt());
                setDate(invoice);
                invoice.setTotalAmount(calculateValue(courseEntity.getDistance(), Integer.parseInt(invoice.getVat())));
                return invoiceMongoDao.saveToMongo(invoice);
            } else {
                throw new IllegalArgumentException("The course is not closed: " + courseEntity.getCourseId());
            }

    }

    private BigDecimal calculateValue(double distance, int vat) {
        var x = kilometerRate.multiply(BigDecimal.valueOf(distance)).setScale(2);
        var vatAmount = x.multiply(BigDecimal.valueOf(vat).divide(BigDecimal.valueOf(100))).setScale(2);
        return x.add(vatAmount).setScale(2);
    }

    private void setDate(Invoice invoice) {
        Date currentDate = new Date();
        invoice.setDateOfIssue(currentDate);
        invoice.setDateOfPayment(setDateOfPayment(currentDate));
    }

    private Date setDateOfPayment(Date currentDate) {
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, 7);
        return calendar.getTime();
    }

    @Override
    public boolean deleteInvoice(int id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("objectId").is(id));
        return invoiceMongoDao.removeInvoice(query);
    }

    @Override
    public Invoice findById(String invoiceId) {
        return invoiceMongoDao.findInvoiceById(invoiceId);
    }

    @Override
    //toDo
    public boolean updateById(String invoiceId, Invoice invoice) {
        return false;
        // invoiceMongoDao.updateInvoiceById();
    }

    @Override
    public List<Invoice> getAll() {
        return invoiceMongoDao.findAllInvoices().stream().sorted(Comparator.comparing(Invoice::getDateOfService)).collect(Collectors.toList());
    }

    @Override
    public Invoice print(String invoiceId) {
        Invoice invoice = findById(invoiceId);
        Optional<Client> client = clientService.getClient(Integer.parseInt(invoice.getClientId()));
        Course course = courseService.findCourse(invoice.getCourseId());
        Optional<Company> company = companyRepository.findById(Integer.valueOf(invoice.getCompanyId()));
        PrintInvoice printInvoice = (PrintInvoice) invoice;
        printInvoice.setClient(client.get());
        printInvoice.setCompany(company.get());
        printInvoice.setCourse(course);
        return printInvoice;
    }

    @Override
    public List<Invoice> findByClient(int clientId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("clientId").is(clientId));
        return invoiceMongoDao.findByInvoices(query);
    }

    @Override
    public List<Invoice> findByUnpaid (Date unpaidDate) {
        Query query = new Query();
        query.addCriteria(Criteria.where("clientId").is(unpaidDate));
        return invoiceMongoDao.findByInvoices(query);
    }
}
