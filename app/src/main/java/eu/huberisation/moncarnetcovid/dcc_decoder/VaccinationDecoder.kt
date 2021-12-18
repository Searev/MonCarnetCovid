package eu.huberisation.moncarnetcovid.dcc_decoder

import com.upokecenter.cbor.CBORObject
import eu.huberisation.moncarnetcovid.entities.Vaccination
import java.text.SimpleDateFormat
import java.util.*

object VaccinationDecoder {
    private const val KEY_DATE = "dt"
    private const val KEY_NB_DOSES = "dn"
    private const val KEY_NB_DOSES_TOTAL = "sd"
    private const val KEY_PRODUIT = "mp"
    private const val KEY_FABRIQUANT = "ma"
    private val PRODUITS = mapOf(
        "EU/1/20/1528" to "Comirnaty",
        "EU/1/20/1507" to "COVID-19 Vaccine Moderna",
        "EU/1/21/1529" to "Vaxzevria",
        "EU/1/20/1525" to "COVID-19 Vaccine Janssen",
    )

    private val FABRIQUANTS = mapOf(
        "ORG-100001699" to "AstraZeneca AB",
        "ORG-100030215" to "Biontech Manufacturing GmbH",
        "ORG-100001417" to "Janssen-Cilag International",
        "ORG-100031184" to "Moderna Biotech Spain S.L.",
        "ORG-100006270" to "Curevac AG",
        "ORG-100013793" to "CanSino Biologics",
        "ORG-100020693" to "China Sinopharm International Corp. - Beijing location",
        "ORG-100010771" to "Sinopharm Weiqida Europe Pharmaceutical s.r.o. - Prague location",
        "ORG-100024420" to "Sinopharm Zhijun (Shenzhen) Pharmaceutical Co. Ltd. - Shenzhen location",
        "ORG-100024420" to "Sinopharm Zhijun (Shenzhen) Pharmaceutical Co. Ltd. - Shenzhen location",
        "ORG-100032020" to "Novavax CZ AS",
        "Gamaleya-Research-Institute" to "Gamaleya Research Institute",
        "Vector-Institute" to "Vector Institute",
        "Sinovac-Biotech" to "Sinovac Biotech",
        "Bharat-Biotech" to "Bharat Biotech",
    )

    fun fromMessage(vaccination: CBORObject): Vaccination? {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)
            .parse(vaccination.getString(KEY_DATE))

        val idProduit = vaccination.getString(KEY_PRODUIT)
        val produit = PRODUITS[idProduit] ?: idProduit

        val idFabriquant = vaccination.getString(KEY_FABRIQUANT)
        val fabriquant = FABRIQUANTS[idFabriquant] ?: idFabriquant

        return Vaccination(
            date,
            vaccination.getInt(KEY_NB_DOSES),
            vaccination.getInt(KEY_NB_DOSES_TOTAL),
            fabriquant,
            produit,
        )
    }
}