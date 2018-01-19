package ru.barabo.observer.config.barabo.crypto.task

import ru.barabo.observer.config.barabo.crypto.CryptoUnCryptoPacket
import ru.barabo.observer.crypto.Verba

object UnCryptoPacket : CryptoUnCryptoPacket("Расшифровать пакет", Verba::unCryptoAndUnSigned)

object CryptoPacket : CryptoUnCryptoPacket("Зашифровать пакет", Verba::signBaraboAndCrypto)