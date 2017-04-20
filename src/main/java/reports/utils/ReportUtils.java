package reports.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Classe de uso genérico com funções reutilizaveis para qualquer sistema.
 *
 * @author Arthemus C. Moreira
 * @since 12/09/2011
 */
public final class ReportUtils {

    /**
     * Para obter uma data sem a descrição de horário.
     *
     * @param date
     *          Data a ser formatada.
     * @return ddMMyyyy 00:00:00
     */
    public static Date getOnlyDate(Date date) {
        if(date == null)
            return null;

        DateTimeZone timeZone = DateTimeZone.forID("America/Sao_Paulo");
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy").withZone(timeZone);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        String dateFormated = format.format(date);
        return formatter.parseLocalDate(dateFormated).toDate();
    }

    public static Date getOnlyDate(String date) {
        return getOnlyDate(ISODateTimeFormat.dateOptionalTimeParser().withOffsetParsed().parseLocalDateTime(date).toDate());
    }

    /**
     * Para obter a data atual sem a descrição de horário.
     *
     * @return ddMMyyyy 00:00:00
     */
    public static Date getOnlyDate() {
        return ReportUtils.getOnlyDate(new Date());
    }

    /**
     * Verifica se um determinado valor existe, impede um saudoso 'NullPointerException'
     *
     * @param value
     *          Valor string a ser validado.
     * @return true or false
     */
    public static boolean isExists(String value) {
        return (value != null && !value.isEmpty() && !"null".equals(value));
    }

}
