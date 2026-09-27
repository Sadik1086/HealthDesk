package com.healthdesk.service;

import java.util.HashMap;
import java.util.Map;


public class BdDrugMapper {

    private static final Map<String, String> BD_TO_GENERIC = new HashMap<>();

    static {
        // Paracetamol / Acetaminophen group
        BD_TO_GENERIC.put("napa",           "Acetaminophen");
        BD_TO_GENERIC.put("napa extra",     "Acetaminophen");
        BD_TO_GENERIC.put("ace",            "Acetaminophen");
        BD_TO_GENERIC.put("ace plus",       "Acetaminophen");
        BD_TO_GENERIC.put("renova",         "Acetaminophen");
        BD_TO_GENERIC.put("paracetamol",    "Acetaminophen");

        // Antacid / PPI group
        BD_TO_GENERIC.put("seclo",          "Omeprazole");
        BD_TO_GENERIC.put("losectil",       "Omeprazole");
        BD_TO_GENERIC.put("omeprazole",     "Omeprazole");
        BD_TO_GENERIC.put("pantoprazole",   "Pantoprazole");
        BD_TO_GENERIC.put("pantonix",       "Pantoprazole");
        BD_TO_GENERIC.put("maxpro",         "Pantoprazole");
        BD_TO_GENERIC.put("esomeprazole",   "Esomeprazole");
        BD_TO_GENERIC.put("nexum",          "Esomeprazole");

        // Antibiotic group
        BD_TO_GENERIC.put("moxacil",        "Amoxicillin");
        BD_TO_GENERIC.put("amoxicillin",    "Amoxicillin");
        BD_TO_GENERIC.put("amoxil",         "Amoxicillin");
        BD_TO_GENERIC.put("azithromycin",   "Azithromycin");
        BD_TO_GENERIC.put("azithro",        "Azithromycin");
        BD_TO_GENERIC.put("zithromax",      "Azithromycin");
        BD_TO_GENERIC.put("ciprofloxacin",  "Ciprofloxacin");
        BD_TO_GENERIC.put("ciprocin",       "Ciprofloxacin");
        BD_TO_GENERIC.put("metronidazole",  "Metronidazole");
        BD_TO_GENERIC.put("amodis",         "Metronidazole");
        BD_TO_GENERIC.put("filmet",         "Metronidazole");

        // NSAID / Pain group
        BD_TO_GENERIC.put("ibuprofen",      "Ibuprofen");
        BD_TO_GENERIC.put("brufen",         "Ibuprofen");
        BD_TO_GENERIC.put("diclofenac",     "Diclofenac");
        BD_TO_GENERIC.put("voltaren",       "Diclofenac");
        BD_TO_GENERIC.put("naproxen",       "Naproxen");

        // Antihistamine group
        BD_TO_GENERIC.put("fexo",           "Fexofenadine");
        BD_TO_GENERIC.put("fexofenadine",   "Fexofenadine");
        BD_TO_GENERIC.put("cetirizine",     "Cetirizine");
        BD_TO_GENERIC.put("alatrol",        "Cetirizine");
        BD_TO_GENERIC.put("loratadine",     "Loratadine");

        // Diabetes group
        BD_TO_GENERIC.put("metformin",      "Metformin");
        BD_TO_GENERIC.put("glucomet",       "Metformin");
        BD_TO_GENERIC.put("glimepiride",    "Glimepiride");

        // Cardiovascular group
        BD_TO_GENERIC.put("amlodipine",     "Amlodipine");
        BD_TO_GENERIC.put("amlovas",        "Amlodipine");
        BD_TO_GENERIC.put("atenolol",       "Atenolol");
        BD_TO_GENERIC.put("losartan",       "Losartan");
        BD_TO_GENERIC.put("lisinopril",     "Lisinopril");
        BD_TO_GENERIC.put("atorvastatin",   "Atorvastatin");
        BD_TO_GENERIC.put("lipitor",        "Atorvastatin");

        // Vitamin / Supplement
        BD_TO_GENERIC.put("cinkara",        "Vitamin C");
        BD_TO_GENERIC.put("zincovit",       "Zinc");
        BD_TO_GENERIC.put("vitamin c",      "Ascorbic Acid");
        BD_TO_GENERIC.put("calcium",        "Calcium Carbonate");
    }


    public static String resolve(String input) {
        if (input == null || input.isBlank()) return null;
        return BD_TO_GENERIC.get(input.trim().toLowerCase());
    }


    public static boolean isBdBrand(String input) {
        if (input == null || input.isBlank()) return false;
        return BD_TO_GENERIC.containsKey(input.trim().toLowerCase());
    }
}
