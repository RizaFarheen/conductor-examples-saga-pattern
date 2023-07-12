package io.orkes.example.saga.service;

import io.orkes.example.saga.dao.PaymentsDAO;
import io.orkes.example.saga.pojos.Payment;
import io.orkes.example.saga.pojos.PaymentMethod;
import io.orkes.example.saga.pojos.PaymentRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaymentService {

    private static final PaymentsDAO paymentsDAO = new PaymentsDAO("jdbc:sqlite:cab_saga.db");

    public static Payment createPayment(PaymentRequest paymentRequest) {
        int riderId = paymentRequest.getRiderId();

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentsDAO.readPaymentMethod(riderId, paymentMethod);

        Payment payment = new Payment();
        payment.setBookingId(paymentRequest.getBookingId());
        payment.setAmount(40.0);
        payment.setPaymentMethodId(0);
        payment.setStatus(Payment.Status.PENDING);
        paymentsDAO.insertPayment(payment);

        // Check for payment methods for the rider
        // If exists, try to make the payment
        if (paymentMethod.getId() > 0) {
            payment.setPaymentMethodId(paymentMethod.getId());
            // Call external Payments API
            if(makePayment(payment)) {
                payment.setStatus(Payment.Status.SUCCESSFUL);
            }
        } else {
            log.error("Payment method for rider {} not available, failed to pay for booking {}", riderId, paymentRequest.getBookingId());
            payment.setErrorMsg("Rider doesn't have a payment method added");
            payment.setStatus(Payment.Status.FAILED);
        }

        paymentsDAO.updatePayment(payment);

        return payment;
    }

    public static boolean cancelPayment(String bookingId) {
        // Cancel Payment in DB
        return true;
    }

    private static boolean makePayment(Payment payment) {
        // Ideally an async call would be made with a callback
        // But, we're skipping that and assuming payment went through
        return true;
    }
}
