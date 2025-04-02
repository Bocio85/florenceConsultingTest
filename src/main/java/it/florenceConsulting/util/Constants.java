package it.florenceConsulting.util;

import java.util.ArrayList;
import java.util.List;

public final class Constants {

    public static final String csvParser =";";

    public static final List<String> userFileColumns = List.of(
            "name","surname","unique_code","contact_number",
            "address","city","zip_code","email");

    public static final Integer columenNumber = userFileColumns.size();
}
