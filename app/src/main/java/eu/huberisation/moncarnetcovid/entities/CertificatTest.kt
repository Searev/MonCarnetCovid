package eu.huberisation.moncarnetcovid.entities

class PasseSanitaire {
    companion object {
        private val validationRegex: Regex = "^[A-Z\\d]{4}" // Les 4 premiers caractères, qui représentent la version du format de document, son ignorés.
            .plus("([A-Z\\d]{4})") // Les 4 suivants représentent l'autorité de certification du document.
            .plus("([A-Z\\d]{4})") // Les 4 suivants sont l'identifiant du certificat utilisé pour signer le document
            .plus("[A-Z\\d]{8}") // Les 8 suivants sont ignorés
            .plus("B2") // Les 2 d'après représentent le type de certificat (sanitaire).
            .plus("[A-Z\\d]{4}") // Characters 22 to 25 are ignored.
            .plus("F0([^\\x1D\\x1E]+)[\\x1D\\x1E]") // 3 - We capture the field F0. It must have at least one character.
            .plus("F1([^\\x1D\\x1E]+)[\\x1D\\x1E]") // 4 - We capture the field F1. It must have at least one character.
            .plus("F2(\\d{8})") // 5 - We capture the field F2. It can only contain digits.
            .plus("F3([FMU]{1})") // 6 - We capture the field F3. It can only contain "F", "M" or "U".
            .plus("F4([A-Z\\d]{3,7})\\x1D?") // 7 - We capture the field F4. It can contain 3 to 7 uppercase letters and/or digits.
            // It can also be ended by the GS ASCII char (29) if the field reaches its max length.
            .plus("F5([PNIX]{1})") // 8 - We capture the field F5. It can only contain "P", "N", "I" or "X".
            .plus("F6(\\d{12})") // 9 - We capture the field F6. It can only contain digits.
            .plus("\\x1F{1}") // This character is separating the message from its signature.
            .plus("([A-Z\\d\\=]+)$").toRegex() // 10 - This is the message signature.
    }
}