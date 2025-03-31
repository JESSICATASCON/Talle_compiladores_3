package Ejercicios;

public class prueba {
    public static void main(String[] args) {
        int edad = 18;
        double costo = 35000;
        double descuento_Mayor = 0.25;
        double valor_conDescuento = 0;
        double valor_T = 0;

        if (edad >= 18 ){
            valor_conDescuento = costo*descuento_Mayor;
            valor_T = costo-valor_conDescuento;
            System.out.println("El valor del procuto es de: "+valor_T);

        }   else {
            System.out.println("""
                    No estas permitido para descuento
                    Costo del producto: """+costo);
        }

    }

}


