package eu.huberisation.moncarnetcovid.entities

class AttestationVaccination: AbstractCertificat() {
    companion object {
        private val validationRegex: Regex = "^[A-Z\\d]{4}" // Characters 0 to 3 are ignored. They represent the document format version.
            .plus("([A-Z\\d]{4})") // 1 - Characters 4 to 7 represent the document signing authority.
            .plus("([A-Z\\d]{4})") // 2 - Characters 8 to 11 represent the id of the certificate used to sign the document.
            .plus("[A-Z\\d]{8}") // Characters 12 to 19 are ignored.
            .plus("L1") // Characters 20 and 21 represent the wallet certificate type (sanitary, ...)
            .plus("[A-Z\\d]{4}") // Characters 22 to 25 are ignored.
            .plus("L0([^\\x1D\\x1E]+)[\\x1D\\x1E]") // 3 - We capture the field L0. It can contain uppercased letters and spaces.
            // It can also be ended by the GS ASCII char (29) if the field reaches its max length.
            .plus("L1([^\\x1D\\x1E]+)[\\x1D\\x1E]") // 4 - We capture the field L1. It must have at least one character.
            .plus("L2(\\d{8})\\x1D?") // 5 - We capture the field L2. It can only contain 8 digits.
            .plus("L3([^\\x1D\\x1E]*)[\\x1D\\x1E]") // // 6 - We capture the field L3. It can contain any characters.
            .plus("L4([^\\x1D\\x1E]+)[\\x1D\\x1E]") // 7 - We capture the field L4. It must have at least one character
            .plus("L5([^\\x1D\\x1E]+)[\\x1D\\x1E]") // 8 - We capture the field L5. It must have at least one character
            .plus("L6([^\\x1D\\x1E]+)[\\x1D\\x1E]") // 9 - We capture the field L6. It must have at least one character
            .plus("L7(\\d{1})") // 10 - We capture the field L7. It can contain only one digit.
            .plus("L8(\\d{1})") // 11 - We capture the field L8. It can contain only one digit.
            .plus("L9(\\d{8})") // 12 - We capture the field L9. It can only contain 8 digits.
            .plus("LA([A-Z\\d]{2})") // 13 - We capture the field LA. 2 characters letters or digits
            .plus("\\x1F{1}") // This character is separating the message from its signature.
            .plus("([A-Z\\d\\=]+)$").toRegex() // 14 - This is the message signature.

        val headerDetectionRegex: Regex = "^[A-Z\\d]{4}" // Characters 0 to 3 are ignored.
            // They represent the document format version.
            .plus("([A-Z\\d]{4})") // 1 - Characters 4 to 7 represent the document signing authority.
            .plus("([A-Z\\d]{4})") // 2 - Characters 8 to 11 represent the id of the certificate used to sign the document.
            .plus("[A-Z\\d]{8}") // Characters 12 to 19 are ignored.
            .plus("L1") // Characters 20 and 21 represent the wallet certificate type (sanitary, ...)
            .toRegex()

        fun correspond(code: String) = validationRegex.matches(code)
    }
}