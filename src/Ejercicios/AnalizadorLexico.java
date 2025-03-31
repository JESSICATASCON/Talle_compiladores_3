package Ejercicios;
import java.util.*;
import java.util.regex.*;
//Funcionamiento
public class AnalizadorLexico {
    private static final Set<String> PALABRAS_RESERVADAS = new HashSet<>(Arrays.asList("int", "float", "double", "if", "while", "return", "public", "class", "static", "void"));
    private static final Set<String> OPERADORES = new HashSet<>(Arrays.asList("+", "-", "*", "/", "=", ">", "<", "==", ">=", "<=", "!="));
    private static final Set<String> DELIMITADORES = new HashSet<>(Arrays.asList(";", "{", "}", "(", ")", ","));

    public static class Token {
        String token;
        String tipoToken;
        int numeroLinea;

        public Token(String token, String tipoToken, int numeroLinea) {
            this.token = token;
            this.tipoToken = tipoToken;
            this.numeroLinea = numeroLinea;
        }
    }

    public static class Simbolo {
        String tipo;
        String token;
        List<Integer> lineas;

        public Simbolo(String tipo, String token) {
            this.tipo = tipo;
            this.token = token;
            this.lineas = new ArrayList<>();
        }
    }

    public static Map<String, Object> analizarCodigo(String codigo) {
        String patronTokens = "(\".*?\"|\\b\\d+\\.\\d+\\b|\\b\\d+\\b|\\b[a-zA-Z_]\\w*\\b|[+\\-*/=<>!]=?|[;{}(),])";
        Pattern pattern = Pattern.compile(patronTokens);
        Matcher matcher = pattern.matcher(codigo);

        List<Token> listaTokens = new ArrayList<>();
        Map<String, Simbolo> tablaSimbolos = new HashMap<>();
        int numeroLinea = 1;
        String tipoActual = null;
        int posicionActual = 0;

        while (matcher.find()) {
            String token = matcher.group();
            String tipoToken;

            String antesDelToken = codigo.substring(posicionActual, matcher.start());
            numeroLinea += antesDelToken.length() - antesDelToken.replace("\n", "").length();
            posicionActual = matcher.end();

            if (PALABRAS_RESERVADAS.contains(token)) {
                tipoToken = "PALABRA_RESERVADA";
                if (token.equals("int") || token.equals("float") || token.equals("double")) {
                    tipoActual = token;
                }
            } else if (OPERADORES.contains(token)) {
                tipoToken = "OPERADOR";
                tipoActual = null;
            } else if (DELIMITADORES.contains(token)) {
                tipoToken = "DELIMITADOR";
                tipoActual = null;
            } else if (token.matches("\".*?\"")) {
                tipoToken = "CADENA";
            } else if (token.matches("\\d+\\.\\d+")) {
                tipoToken = "NÚMERO_DECIMAL";
            } else if (token.matches("\\d+")) {
                tipoToken = "NÚMERO_ENTERO";
            } else {
                tipoToken = "IDENTIFICADOR";
                if (!tablaSimbolos.containsKey(token)) {
                    tablaSimbolos.put(token, new Simbolo(tipoActual != null ? tipoActual : "Variable", token));
                }
                tablaSimbolos.get(token).lineas.add(numeroLinea);
                tipoActual = null;
            }
            listaTokens.add(new Token(token, tipoToken, numeroLinea));
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("listaTokens", listaTokens);
        resultado.put("tablaSimbolos", tablaSimbolos);
        return resultado;
    }

    public static void main(String[] args) {
        String codigoEjemplo = """
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
                                    System.out.println(""\"
                                            No estas permitido para descuento
                                            Costo del producto: ""\"+costo);
                                }
                        
                            }
                        
                        }
                        
                """;

        Map<String, Object> resultado = analizarCodigo(codigoEjemplo);
        List<Token> listaTokens = (List<Token>) resultado.get("listaTokens");
        Map<String, Simbolo> tablaSimbolos = (Map<String, Simbolo>) resultado.get("tablaSimbolos");

        System.out.println("\nTokens:");
        for (Token token : listaTokens) {
            System.out.printf("%-15s %-20s %d\n", token.token, token.tipoToken, token.numeroLinea);
        }

        System.out.println("\nTabla de Símbolos:");
        for (Map.Entry<String, Simbolo> entry : tablaSimbolos.entrySet()) {
            System.out.printf("%-15s %-15s %s\n", entry.getKey(), entry.getValue().tipo, entry.getValue().lineas);
        }
    }
}