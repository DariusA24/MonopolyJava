module org.darius_a.monopoly {
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    exports gameset.functionality to com.fasterxml.jackson.databind;
    opens gameset.functionality to com.fasterxml.jackson.databind;
    exports gameset.cards to com.fasterxml.jackson.databind;
    opens gameset.cards to com.fasterxml.jackson.databind;
}