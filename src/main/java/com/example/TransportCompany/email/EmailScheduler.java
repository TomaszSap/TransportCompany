package com.example.TransportCompany.email;

import com.example.TransportCompany.model.Invoice;
import com.example.TransportCompany.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class EmailScheduler {
    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private EmailSender emailSender;

    @Scheduled(cron ="${email_scheluder_cron}")
    public void sendEmailsAfterOneWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        Date oneWeekAgo = calendar.getTime();

        List<Invoice> invoicesToSendEmail = invoiceService.findByUnpaid(oneWeekAgo);

        for (Invoice invoice : invoicesToSendEmail) {
            String recipient = invoice.getClientEmail();
            String subject = "Payment reminder invoice number"+invoice.getObjectId();
            String text = """            
                        Reminder to pay the invoice %s
                        Amount to pay: %s
                        Please make payment as soon as possible.
                        """.formatted(invoice.getObjectId(),invoice.getTotalAmount());

            emailSender.sendEmail(recipient, subject, text);
        }
    }
}
