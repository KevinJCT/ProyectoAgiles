package interfaces;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Fecha {

    Calendar fecha = new GregorianCalendar();
    Calendar fecha1 = new GregorianCalendar();

    public Calendar getFecha1() {
        return fecha1;
    }

    public void setFecha1(Calendar fecha1) {
        this.fecha1 = fecha1;
    }
    //Para el Menu
    public String fecha() {

        int año = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH);
        int dia = fecha.get(Calendar.DAY_OF_MONTH);

        return dia + "/" + (mes + 1) + "/" + año;
    }
    //Para el Menu
    public String Hora() {
        int hora = fecha.get(Calendar.HOUR_OF_DAY);
        int minuto = fecha.get(Calendar.MINUTE);
        int segundo = fecha.get(Calendar.SECOND);

        return hora + ":" + minuto + ":" + segundo;
    }

    public String fechaEntrada() {
        int año = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH);
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        int hora = fecha.get(Calendar.HOUR_OF_DAY);
        int minuto = fecha.get(Calendar.MINUTE);
        int segundo = fecha.get(Calendar.SECOND);

        return dia + "/" + (mes + 1) + "/" + año + " a las " + hora + ":" + minuto + ":" + segundo;
    }
    
        public String fechaSalida() {
   
        int año = fecha1.get(Calendar.YEAR);
        int mes = fecha1.get(Calendar.MONTH);
        int dia = fecha1.get(Calendar.DAY_OF_MONTH);
        int hora = fecha1.get(Calendar.HOUR_OF_DAY);
        int minuto = fecha1.get(Calendar.MINUTE);
        int segundo = fecha1.get(Calendar.SECOND);

        return dia + "/" + (mes + 1) + "/" + año + " a las " + hora + ":" + minuto + ":" + segundo;
    }

    public Calendar convertirCadenaToCalenda(String fecha) {
        //Obtiene la instancia del Calendario
        Calendar cal = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
            cal.setTime(sdf.parse(fecha));
        } catch (ParseException ex) {
            Logger.getLogger(Fecha.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cal;
    }
}
