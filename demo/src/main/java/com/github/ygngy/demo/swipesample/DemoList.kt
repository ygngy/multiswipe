/**
 * sample data for demo app
 */

package com.github.ygngy.demo.swipesample


data class ListItem(val id: Int, var liked: Boolean, var data: String){
    fun toggleLike(){
        liked = !liked
    }
}

const val TWO_LEFT_SWIPE_ONE_RIGHT_SWIPE = "Two Left One Right Swipe"
const val ONE_LEFT_SWIPE_TWO_RIGHT_SWIPE = "One Left Two Right Swipe"
const val NO_LEFT_SWIPE = "No Left Swipe"
const val NO_RIGHT_SWIPE = "No Right Swipe"

/**
 * a sample list about flowers for recycler view
 */
val demoList = arrayOf(
        ListItem(1, true, "Rose"),
        ListItem(2, false, "Sunflower"),
        ListItem(3, false, "Moonflower"),
        ListItem(4, false, "Gardenia"),
        ListItem(5, true, "Candytuft"),
        ListItem(6, false, "Orchid"),
        ListItem(7, true, "Lily"),
        ListItem(8, false, "Calla Lily"),
        ListItem(9, false, "Peruvian Lily"),
        ListItem(10, false, "Tulip"),
        ListItem(11, false, "Gladiolus"),
        ListItem(12, false, "Anemone"),
        ListItem(13, false, "Daffodil"),
        ListItem(14, false, "Carnation"),
        ListItem(15, false, "Hyacinth"),
        ListItem(16, false, "Gaura"),
        ListItem(14, false, "Gerbera"),
        ListItem(17, false, "Gaillardia"),
        ListItem(18, false, "Mandevilla"),
        ListItem(19, false, "Chrysanthemum"),
        ListItem(20, false, TWO_LEFT_SWIPE_ONE_RIGHT_SWIPE),
        ListItem(21, false, ONE_LEFT_SWIPE_TWO_RIGHT_SWIPE),
        ListItem(22, false, NO_LEFT_SWIPE),
        ListItem(23, false, NO_RIGHT_SWIPE)

)

/**
 * a sample list about flowers for recycler view
 */
val demoDetailList = arrayOf(
        ListItem(1, false, "A rose is a woody perennial flowering plant of the genus Rosa, in the family Rosaceae, or the flower it bears. There are over three hundred species and tens of thousands of cultivars. They form a group of plants that can be erect shrubs, climbing, or trailing, with stems that are often armed with sharp prickles. Flowers vary in size and shape and are usually large and showy, in colours ranging from white through yellows and reds. Most species are native to Asia, with smaller numbers native to Europe, North America, and northwestern Africa. Species, cultivars and hybrids are all widely grown for their beauty and often are fragrant. Roses have acquired cultural significance in many societies. Rose plants range in size from compact, miniature roses, to climbers that can reach seven meters in height. Different species hybridize easily, and this has been used in the development of the wide range of garden roses."),
        ListItem(2, true, "Dianthus caryophyllus is a herbaceous perennial plant growing up to 80 cm (31 1⁄2 in) tall. The leaves are glaucous greyish green to blue-green, slender, up to 15 cm (6 in) long. The flowers are produced singly or up to five together in a cyme; they are around 3–5 cm (1 1⁄4–2 in) diameter, and sweetly scented; the original natural flower color is bright pinkish-purple, but cultivars of other colors, including red, white, yellow, blue and green, along with some white with colored striped variations have been developed. The fragrant, hermaphrodite flowers have a radial symmetry. The four to six surrounding the calyx, egg-shaped, sting-pointed scales leaves are only ¼ as long as the calyx tube."),
        ListItem(3, true, "Tulips (Tulipa) form a genus of spring-blooming perennial herbaceous bulbiferous geophytes (having bulbs as storage organs). The flowers are usually large, showy and brightly colored, generally red, pink, yellow, or white (usually in warm colors). They often have a different colored blotch at the base of the tepals (petals and sepals, collectively), internally. Because of a degree of variability within the populations, and a long history of cultivation, classification has been complex and controversial. The tulip is a member of the lily family, Liliaceae, along with 14 other genera, where it is most closely related to Amana, Erythronium and Gagea in the tribe Lilieae. There are about 75 species, and these are divided among four subgenera. The name tulip is thought to be derived from a Persian word for turban, which it may have been thought to resemble. Tulips originally were found in a band stretching from Southern Europe to Central Asia, but since the seventeenth century have become widely naturalised and cultivated (see map). In their natural state they are adapted to steppes and mountainous areas with temperate climates. Flowering in the spring, they become dormant in the summer once the flowers and leaves die back, emerging above ground as a shoot from the underground bulb in early spring."),
        ListItem(4, false, "Zantedeschia aethiopica is a rhizomatous herbaceous perennial plant, evergreen where rainfall and temperatures are adequate, deciduous where there is a dry season. Its preferred habitat is in streams and ponds or on the banks. It grows to 0.6–1 m (2.0–3.3 ft) tall, with large clumps of broad, arrow shaped dark green leaves up to 45 cm (18 in) long. The inflorescences are large and are produced in spring, summer and autumn, with a pure white spathe up to 25 cm (9.8 in) and a yellow spadix up to 90 mm (3 1⁄2 in) long. The spadix produces a faint, sweet fragrance."),
        ListItem(5, false, "The Orchidaceae are a diverse and widespread family of flowering plants, with blooms that are often colourful and fragrant, commonly known as the orchid family. Along with the Asteraceae, they are one of the two largest families of flowering plants. The Orchidaceae have about 28,000 currently accepted species, distributed in about 763 genera. The determination of which family is larger is still under debate, because verified data on the members of such enormous families are continually in flux. Regardless, the number of orchid species is nearly equal to the number of bony fishes, more than twice the number of bird species, and about four times the number of mammal species."),
        ListItem(6, false, "Dianthus caryophyllus is a herbaceous perennial plant growing up to 80 cm (31 1⁄2 in) tall. The leaves are glaucous greyish green to blue-green, slender, up to 15 cm (6 in) long. The flowers are produced singly or up to five together in a cyme; they are around 3–5 cm (1 1⁄4–2 in) diameter, and sweetly scented; the original natural flower color is bright pinkish-purple, but cultivars of other colors, including red, white, yellow, blue and green, along with some white with colored striped variations have been developed. The fragrant, hermaphrodite flowers have a radial symmetry. The four to six surrounding the calyx, egg-shaped, sting-pointed scales leaves are only ¼ as long as the calyx tube."),
        ListItem(7, false, "Hyacinthus is a small genus of bulbous, spring-blooming perennials. They are fragrant flowering plants in the family Asparagaceae, subfamily Scilloideae and are commonly called hyacinths /ˈhaɪəsɪnθs/. The genus is native to the area of the eastern Mediterranean from the north of Bulgaria through to the northern part of the region of Palestine."),
        ListItem(8, false, "Helianthus (/ˌhiːliˈænθəs/) is a genus comprising about 70 species of annual and perennial flowering plants in the daisy family Asteraceae. Except for three South American species, the species of Helianthus are native to North America and Central America. The common names sunflower and common sunflower typically refer to the popular annual species Helianthus annuus, whose round flower heads in combination with the ligules look like the sun.")
)