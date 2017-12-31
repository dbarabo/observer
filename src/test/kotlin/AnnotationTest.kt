
import javafx.application.Application
import javafx.scene.control.Alert
import org.junit.Test
import org.slf4j.LoggerFactory
import ru.barabo.db.annotation.ColumnName
import ru.barabo.db.annotation.SequenceName
import ru.barabo.db.annotation.TableName
import ru.barabo.observer.config.cbr.correspondent.task.DownLoadToCorrespond
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.GroupElem
import ru.barabo.observer.store.StoreListener
import ru.barabo.observer.store.derby.StoreDerby
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.*
import kotlin.reflect.jvm.javaType
import ru.barabo.observer.store.State
import java.lang.Error


class AnnotationTest {

    private val logger = LoggerFactory.getLogger(AnnotationTest::class.java)

    //@Test
    fun test() {
        logger.info("test start");

        for (member in Elem::class.java.declaredFields) {
            logger.info("Property=" + member.toString())

            val an = member.getAnnotation(ColumnName::class.java)
            if(an != null) {
                logger.info("ColumnName=" + an.name)
            }
        }
        logger.info("test end");
    }

    //@Test
    fun classToString() {

        val elem  =getElem()

        logger.info(elem.javaClass.canonicalName)
    }

    //@Test
    fun readTree() {
       // logger.info(StoreDerby.root.toString())

    }

    class TestStoreListener(var root: GroupElem = GroupElem()) :StoreListener {

        private val logger = LoggerFactory.getLogger(TestStoreListener::class.java)

        override fun refreshAll(root : GroupElem) {
            this.root = root
            logger.info(this.root.toString())
        }
    }


    //@Test
    fun readTest() {

        val storeListener = TestStoreListener()

        //ExceptionInInitializerError
        //java.lang.ClassNotFoundException

        try {
            StoreDerby.addStoreListener(storeListener)
        } catch (e: Error) {
            logger.error("readTest", e)
            Application.launch()
            Alert(Alert.AlertType.ERROR, "ERROR " + e.message)
        }


        //logger.info(storeListener.root.toString())

        logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")

//        storeListener.root.child.forEach {cfg ->
//            cfg.child.forEach {
//                grp -> grp.child.forEach { item -> logger.info(item.elem.toString()) }
//            }
//        }
    }

    //@Test
    fun updateTest() {

        val storeListener = TestStoreListener()

        StoreDerby.addStoreListener(storeListener)

        synchronized(storeListener.root.child) {
            val item = storeListener.root.child[0].child[0].child[0]

            item.elem.state = State.OK

            StoreDerby.save(item.elem)
        }
    }

    //@Test
    fun saveBase() {

        StoreDerby.save(getElem() )
    }

    private fun getElem() :Elem {
//        val  actionTask = object : ActionTask {
//            override fun name(): String = "Test"
//
//            override fun getConfig(): ConfigTask = Correspondent
//        }

        return Elem(name="Test2",task = DownLoadToCorrespond, path ="c:/test2")
    }

    //@Test
    fun testReflection() {

        val newObject = Class.forName("ru.barabo.observer.store.derby.DerbyQuery").newInstance()
    }



    //@Test
    fun testObject() {
        logger.info("testObject start");

        logger.info(getElem()::class.findAnnotation<TableName>()?.name)

        logger.info("testObject end")
    }

    //@Test
    fun testClass() {
        logger.info("testClass start");

        val item = getElem()

        for (member in item::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>() ) {

            logger.info(member.name)

            val annotationName = member.findAnnotation<SequenceName>()

            if(annotationName?.name != null){

                logger.info(annotationName?.name)

                logger.info(member.name)

                val x = 1

                logger.info(member.returnType.javaType.toString())

                val value =
                when (member.returnType.javaType) {

                    Byte::class.javaPrimitiveType, Byte::class.javaObjectType ->  (x as Number).toByte()
                    Short::class.javaPrimitiveType, Short::class.javaObjectType ->  (x as Number).toShort()
                    Int::class.javaPrimitiveType, Int::class.javaObjectType ->  (x as Number).toInt()
                    Long::class.javaObjectType ->  (x as Number).toLong()
                    Double::class.javaPrimitiveType, Double::class.javaObjectType ->  (x as Number).toDouble()
                    Float::class.javaPrimitiveType, Float::class.javaObjectType ->  (x as Number).toFloat()
                    String::class.javaPrimitiveType, String::class.javaObjectType -> x.toString()
                    else -> null
                }

                (member as KMutableProperty<*>).setter.call(item, value)

                //(this as KTypeImpl).type
                //jvmErasure.javaObjectType::class.javaObjectType.

                logger.info("$item")

               //(member as KMutableProperty<*>).setter.call(item, 1)

                //val valueSequence = getNextSequenceValue(annotationName.name)

                //val setter = member.

                //(member as KProperty1Impl).setter.call(item, valueSequence)

                return
            }
        }

        logger.info("testClass end")
    }
}