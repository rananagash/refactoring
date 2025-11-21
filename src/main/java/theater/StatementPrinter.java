package theater;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * This class generates a statement for a given invoice of performances.
 */
public class StatementPrinter {
    public final Invoice invoice;
    public final Map<String, Play> plays;

    /**
     * Constructs a StatementPrinter with invoice and plays.
     *
     * @param invoice the invoice to print
     * @param plays the map of plays
     */
    public StatementPrinter(final Invoice invoice, final Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
    }

    /**
     * Returns a formatted statement of the invoice associated with this printer.
     * @return the formatted statement
     * @throws RuntimeException if one of the play types is not known
     */
    public String statement() {
        StringBuilder result = new StringBuilder("Statement for "
                + invoice.getCustomer() + System.lineSeparator());

        for (Performance performance : invoice.getPerformances()) {
            result.append(String.format("  %s: %s (%s seats)%n",
                    getPlay(performance).name, usd(getAmount(performance)), performance.audience));
        }
        result.append(String.format("Amount owed is %s%n", usd(getTotalAmount())));
        result.append(String.format("You earned %s credits%n", getTotalVolumeCredits()));
        return result.toString();
    }

    private Play getPlay(Performance performance) {
        return plays.get(performance.playID);
    }

    private int getAmount(Performance performance) {
        int thisAmount = 0;
        switch (getPlay(performance).type) {
            case "tragedy":
                thisAmount = Constants.TRAGEDY_BASE_AMOUNT;
                if (performance.audience > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                    thisAmount += Constants.TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.audience - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
                }
                break;
            case "comedy":
                thisAmount = Constants.COMEDY_BASE_AMOUNT;
                if (performance.audience > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                    thisAmount += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                            + (Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.audience - Constants.COMEDY_AUDIENCE_THRESHOLD));
                }
                thisAmount += Constants.COMEDY_AMOUNT_PER_AUDIENCE * performance.audience;
                break;
            default:
                throw new RuntimeException(String.format("unknown type: %s", getPlay(performance).type));
        }
        return thisAmount;
    }

    private int getVolumeCredits(Performance performance) {
        int volumeCredits = Math.max(performance.audience - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
        // add extra credit for every five comedy attendees
        if ("comedy".equals(getPlay(performance).type)) {
            volumeCredits += performance.audience / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }
        return volumeCredits;
    }

    private int getTotalAmount() {
        int totalAmount = 0;
        for (Performance performance : invoice.getPerformances()) {
            totalAmount += getAmount(performance);
        }
        return totalAmount;
    }

    private int getTotalVolumeCredits() {
        int volumeCredits = 0;
        for (Performance performance : invoice.getPerformances()) {
            volumeCredits += getVolumeCredits(performance);
        }
        return volumeCredits;
    }

    private String usd(int amount) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount / Constants.PERCENT_FACTOR);
    }

    /**
     * Returns the invoice.
     *
     * @return the invoice
     */
    public Invoice getInvoice() {
        return invoice;
    }

    /**
     * Returns the plays map.
     *
     * @return the plays map
     */
    public Map<String, Play> getPlays() {
        return plays;
    }
}