package ru.barabo.observer.config.barabo.plastic

import ru.barabo.observer.config.barabo.plastic.release.task.GetIiaAccept
import ru.barabo.observer.config.barabo.plastic.release.task.GetOciData
import ru.barabo.observer.config.barabo.plastic.release.task.GetOiaConfirm
import ru.barabo.observer.config.barabo.plastic.release.task.MoveHcardIn
import ru.barabo.observer.config.barabo.plastic.turn.task.*
import ru.barabo.observer.config.task.ActionTask

enum class HcardInObject(private val regExp: Regex, private val actionTask: ActionTask) {

    AFP("AFP20\\d\\d\\d\\d\\d\\d_0226\\.\\d\\d\\d\\d".toRegex(RegexOption.IGNORE_CASE), LoadAfp),
    CTL("(C|M)TL.*_0226.*".toRegex(RegexOption.IGNORE_CASE), LoadCtlMtl),
    OBI("OBI_\\d\\d\\d\\d\\d\\d\\d\\d_\\d\\d\\d\\d\\d\\d_0226_GC_FEE".toRegex(RegexOption.IGNORE_CASE), LoadObi),
    OBR("OBR_.*".toRegex(RegexOption.IGNORE_CASE), LoadObr),
    ACC("ACC_\\d{8}_\\d{6}_0226".toRegex(RegexOption.IGNORE_CASE), LoadRestAccount),
    IIA_ACCEPT("IIA_........_......_0226\\.(ACCEPT|ERROR)".toRegex(RegexOption.IGNORE_CASE), GetIiaAccept),
    OIA("OIA_........_......_0226".toRegex(RegexOption.IGNORE_CASE), GetOiaConfirm),
    OCI("OCI_........_......_0226".toRegex(RegexOption.IGNORE_CASE), GetOciData),
    ACQ("AFP_ACQ20\\d\\d\\d\\d\\d\\d_0226\\.\\d\\d\\d\\d".toRegex(RegexOption.IGNORE_CASE), LoadAcq),
    MOVE_ALL("НИКОГДА_НЕ_ВЫПОЛНИТСЯ".toRegex(RegexOption.IGNORE_CASE), MoveHcardIn);

    companion object {

        fun isInclude(actionTask: ActionTask): Boolean =
                HcardInObject.values().firstOrNull {actionTask === it.actionTask} != null

        fun objectByFileName(fileName: String, otherTask: ActionTask): ActionTask {

            return HcardInObject.values().firstOrNull { it.regExp.matches(fileName) }?.actionTask
                    ?: otherTask
        }
    }
}