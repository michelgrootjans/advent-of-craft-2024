package email

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import java.nio.file.Paths
import java.security.MessageDigest
import java.util.*
import kotlin.io.path.readText

class EncryptionTest : StringSpec({
    val encryption = Encryption(
        Configuration(
            key = convertKey("Advent Of Craft"),
            iv = convertIv("2024")
        )
    )

    "should encrypt a string" {
        encryption
            .encrypt("Unlock Your Potential with the Advent Of Craft Calendar!")
            .shouldBe("L7wht/YddOoTvYvrc+wFcZhtXNvZ2cHFxq9ND27h1Ovv/aWLxN8lWv1xMsguM/R4Yodk3rn9cppI+YarggtPjA==")
    }

    "should decrypt a string" {
        encryption
            .decrypt("L7wht/YddOoTvYvrc+wFcZhtXNvZ2cHFxq9ND27h1Ovv/aWLxN8lWv1xMsguM/R4Yodk3rn9cppI+YarggtPjA==")
            .shouldBe("Unlock Your Potential with the Advent Of Craft Calendar!")
    }

    "solution to day 0" {
        encryption
            .decrypt(
                "JFNKPSVfvNhhIRPSFNaH4JBZTmZFk54f+c3tN/gSoffZ3DrrvxvcQAtDXJSuZBu7axRLGCE+HD+ismHtMnrnYtbCpQZxwclovn0H5BY06rE27xh40/uJTa1K6UCFBM/5oN7xbxJXxHQ209P6F6fRt7aCtn+AGA4ke1dGAqbcmjHl71GNdWWxqiRW6W1Dak6+jlUfm685FHfeUt2pSOKzDAmkb8qRcnCBOPRKDtroqjqxpMJCAf6cs/M4tu7xizOWJlrd0qRH2/qyoD2wuJ7X9/Y53o+v1KMxsWoL0t/7UJ7SOYwykNIxhwd7jrGT08ubKZfa0/8uYb0/qi5vRvl9oov5XxOBXwvaa1/yFy+giidR/ORsjN3xgTm5JuNJelQnY12qW3VX0w+N6srhqDGyMa31Sj//SPIEPHJApHMoGEWNKODJ1mevKgt4kJY6oXdZjefT08vQX3YdYadlNnnqm2ieR5Z/49LGo1tI11+PcSmQ3sTQMqhM4WRSijCFuA339wi2o7drz27QploRkENu8z47xi5rOoGFfIN9xiQsvsDeGVfhQq1P7ooT4daYkirvIhaDiPjgIRiydAKKL0jGt6QX2A841XI1aV+8hNpLhP7rbcNYsUTxkWZT0LKX2We/EKz/6LvewF4ZXxJo+yCntfYhxJsHKVwINm2owkbMJplH/BDBJmpousnQcPzMvpUFvE8u+LN6zCTf4s/stcen3wVcCjWiInFAPJCjSqU8xXy0Y6B7ReK50M+HCfuW+AM/FozOVD76En+7E15BBHmVZQwGsJlBrKKhniXFBiAelSM7KMSdXvYl1TMUSPqh0NlXrnqLPCWp3rMoE8fhhqawZ7oHAvdKQWQqtUY5Cb/IbN61mPLPaPLnBAKxxXLSQMf4b7iQnKc764w99o4hgs9lq9s6NFs80OT1ex54Lp49M7lN9Rxg6VtxDraA22G4j+rdBKryoxDd5K4f1avS1PJ9bTmmgjb+26VDMtrJ5bWIPuW0EroZCZgtQwzGeoXOQ3dP93rLyw9ytcQiJSB3OLs7LPj0wW/Y8HWsV6FhV9C75397owaQAarPg6R1SU6osoOJDAHqYC+WRzVwnl28w1YYBmiMvsNimmq0Z4lmHdtZKY5VSNas1cuSXUj0RCH5SHi6gZKKxMGpHJmHZ7lqbfOQlyUQVyJ7yg9rBWYvDzkZLe/t1VdllenbYv1KMkkcoCkF5sNKE1x8CjU6d99XF5fCSjj/S74joAcm1OW8RGJIUQis4DweJy+0Us/NU7lzyYdlBkPND7kQ+fO/wwCs4PmBf5zHIZirLsFy2kJ8x6s3ky/FEkx+iiXWZ896JPd5kbUeL9DC1aMZ9BOww0unjIrEk/nH619TokSW11A5DL4o5Q1ofjIkSq/PbfKhsq8B7OznDUcNu4896t/4/59S1BK84nrJErgZyQ0fEwlVlcd89os="
            )
            .shouldBe(
                """Dear consultant,

We are facing an unprecedented challenge in Christmas Town.

The systems that keep our magical operations running smoothly are outdated, fragile, and in dire need of modernization. 
We urgently require your expertise to ensure Christmas happens this year.
Our town is located within a mountain circlet at the North Pole, surrounded by high peaks and protected by an advanced communication and shield system to hide it from the outside world.

You have been selected for your exceptional skills and dedication. 
Please report to the North Pole immediately. 

Enclosed are your travel details and a non-disclosure agreement that you must sign upon arrival.
Our dwarf friends from the security will receive and escort you in as soon as you check security.
In the following days, you will receive bracelets to be able to pass through the magic shield.

Time is of the essence.
You must arrive before the beginning of December to be able to acclimate yourself with all the systems.

We are counting on you to help save Christmas.

Sincerely,

Santa Claus 🎅"""
            )
    }

    //It is a Property-Based test that checks the below property
    //I'm pretty sure we will talk about this concept during our Journey 🎅
    "for all x (x: valid string) -> decrypt(encrypt(x)) == x" {
        checkAll(Arb.string(1..100)) { plainText ->
            encryption.decrypt(
                encryption.encrypt(plainText)
            ) shouldBe plainText
        }
    }
})

private fun convertKey(key: String): String {
    val sha256 = MessageDigest.getInstance("SHA-256")
    val keyBytes = sha256.digest(key.toByteArray(Charsets.UTF_8))
    return Base64.getEncoder().encodeToString(keyBytes)
}

private fun convertIv(iv: String): String {
    val md5 = MessageDigest.getInstance("MD5")
    val ivBytes = md5.digest(iv.toByteArray(Charsets.UTF_8))
    return Base64.getEncoder().encodeToString(ivBytes)
}

private fun loadFile(fileName: String): String =
    Paths.get(requireNotNull(EncryptionTest::class.java.classLoader.getResource(fileName)) {
        "File not found: $fileName"
    }.toURI()).readText(Charsets.UTF_8)