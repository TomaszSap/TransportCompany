package com.example.TransportCompany.services;

import com.example.TransportCompany.Mongo.InvoiceMongoDao;
import com.example.TransportCompany.model.Client;
import com.example.TransportCompany.model.Course;
import com.example.TransportCompany.model.Invoice;
import com.example.TransportCompany.model.PrintInvoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
public class InvoiceServiceImpl implements  InvoiceService{
    @Autowired
    CourseService courseService;
    @Autowired
    ClientService clientService;
    @Autowired
    InvoiceMongoDao invoiceMongoDao;
    private final Calendar calendar = Calendar.getInstance();
    @Value("${per_km_rate}")
    private BigDecimal kilometerRate;

    @Override
    public Invoice addInvoice(Invoice invoice,int courseId) {
        Course courseEntity=courseService.findCourse(courseId);
        if(courseEntity.getType().getName()=="CLOSED"){
            invoice.setCourseId(courseEntity.getCourseId());
        invoice.setClientId(String.valueOf(courseEntity.getClientsId()));
        invoice.setDateOfService(courseEntity.getUpdatedAt());
        setDate(invoice);
        invoice.setValue(calculateValue(courseEntity.getDistance()));
        return invoiceMongoDao.saveToMongo(invoice);}
        else {
            throw new IllegalArgumentException("The course is not closed: " + courseEntity.getCourseId());
        }
    }

    private BigDecimal calculateValue(double distance) {
        BigDecimal ratePerKilometer = kilometerRate.multiply(BigDecimal.valueOf(distance));
        return ratePerKilometer;
    }

    private void setDate(Invoice invoice)
    {
        Date currentDate = new Date();
        invoice.setDateOfIssue(currentDate);
       invoice.setDateOfPayment(setDateOfPayment(currentDate));
    }
    private Date setDateOfPayment(Date currentDate)
    {
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, 7);
        return calendar.getTime();
    }
    @Override
    public boolean deleteInvoice(int id) {
        Query query=new Query();
        query.addCriteria(Criteria.where("objectId").is(id));
        return invoiceMongoDao.removeInvoice(query);
    }

    @Override
    public Invoice findById(String invoiceId) {
      return invoiceMongoDao.findInvoiceById(invoiceId);
    }

    @Override
    //toDo
    public boolean updateById(String invoiceId,Invoice invoice) {
        return false;
       // invoiceMongoDao.updateInvoiceById();
    }

    @Override
    public List<Invoice> getAll(Invoice invoice) {
        return null;
    }

    @Override
    public Invoice print(String invoiceId) {
        Invoice invoice=findById(invoiceId);
        Client client=clientService.getClient(invoice.getClientId());
        Course course=courseService.findCourse(invoice.getCourseId());
        PrintInvoice printInvoice= (PrintInvoice) invoice;
        printInvoice.setClient(client);
        printInvoice.setCourse(course);
        return printInvoice;
    }

    @Override
    public List<Invoice> findByClient(String clientId) {
        Query query=new Query();
        query.addCriteria(Criteria.where("clientId").is(clientId));
      return invoiceMongoDao.findByClientInvoices(query);
    }
}
