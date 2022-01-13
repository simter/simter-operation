package tech.simter.operation

import org.springframework.data.domain.Persistable

/**
 * For kotlin-1.6+ compile error review for spring `Persistable<ID>` interface.
 */
class Test {
  init {
    println(D1("d").id)
    println(D2("d").getId())
    println(D2("d").id)
    println(C1("c").id)
    println(C2().id)

    val e: E = D3("d")
    println(e.id)
  }
}

interface E : Persistable<String> {
  override fun getId(): String
}

data class D3(private val id: String) : E {
  override fun getId(): String {
    return id
  }

  override fun isNew(): Boolean {
    TODO("Not yet implemented")
  }
}

data class D2(@JvmField internal val id: String, val t: String = "") : Persistable<String> {
  override fun getId(): String {
    return id
  }

  override fun isNew(): Boolean {
    TODO("Not yet implemented")
  }
}

data class D1(private val id: String) : Persistable<String> {
  override fun getId(): String {
    return id
  }

  override fun isNew(): Boolean {
    TODO("Not yet implemented")
  }
}

class C2 : Persistable<String> {
  @get:JvmName("getOtherId")
  val id: String = "t"
  override fun getId(): String {
    return id
  }

  override fun isNew(): Boolean {
    TODO("Not yet implemented")
  }
}

class C1(
  @get:JvmName("getOtherId")
  val id: String
) : Persistable<String> {
  override fun getId(): String {
    return id
  }

  override fun isNew(): Boolean {
    TODO("Not yet implemented")
  }
}