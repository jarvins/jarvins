package com.jarvins.builder;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class Bill {

    private int period;

    private BigDecimal shouldAmount;

    private BigDecimal shouldPrincipal;

    private BigDecimal shouldInterest;

    private BigDecimal shouldOverdue;

    private LocalDate shouldRepayDate;

    private BigDecimal remainPrincipal;

    @Override
    public String toString() {
        return "Bill{" +
                "period=" + period +
                ", shouldAmount=" + shouldAmount +
                ", shouldPrincipal=" + shouldPrincipal +
                ", shouldInterest=" + shouldInterest +
                ", shouldOverdue=" + shouldOverdue +
                ", shouldRepayDate=" + shouldRepayDate +
                ", remainPrincipal=" + remainPrincipal +
                '}';
    }
}
