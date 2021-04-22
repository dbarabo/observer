package ru.barabo.observer.config.task.p440.out.xml.ver4.bnp;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.data.BnpResponseDataVer4;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.SenderBank;
import ru.barabo.observer.config.task.p440.out.xml.ver4.InfoBankPBRVer4;

import java.util.Date;

@XStreamAlias("СБЩБННЕИСП")
public class BnpInfoPartVer4 {

        @XStreamAlias("НомСообщ")
        private String numberMessage = (System.currentTimeMillis() / 100) % 10000000 + "";

        @XStreamAlias("НомерПоруч")
        private String numberPno;

        @XStreamAlias("ДатаПоруч")
        private String datePno;

        @XStreamAlias("КодНОБ")
        private String codeFns;

        @XStreamAlias("СумПлат")
        private String sumPnoKopeika;

        @XStreamAlias("ИНННП")
        private String innPayer;

        @XStreamAlias("КППНП")
        private String kppPayer;

        @XStreamAlias("Плательщ")
        private String namePayer;

        @XStreamAlias("СумЧаст")
        private String sumPartExecKopeika;

        @XStreamAlias("КодПричНеисп")
        private Integer codeWhyNotExecute = 1;

        @XStreamAlias("ДопСвед")
        private String descriptionWhyNotExecute;

        @XStreamAlias("ДатаНаправ")
        private String dateSend = XmlLoader.formatDate(new Date());

        @XStreamAlias("НомСчет")
        private String account;

        @XStreamAlias("СведБанкПБР")
        final private InfoBankPBRVer4 bank = InfoBankPBRVer4.ourBank();

        public BnpInfoPartVer4(BnpResponseDataVer4 responseData) {

            this.numberPno = responseData.getNumberPno();

            this.datePno = XmlLoader.formatDate(responseData.getDatePno());

            this.codeFns = responseData.getCodeFns();

            this.sumPnoKopeika = XmlLoader.formatInteger(responseData.getSumPnoKopeika());

            this.innPayer = responseData.getPayer().getInn();

            this.kppPayer = responseData.getPayer().getKpp() == null ||
                    responseData.getPayer().getKpp().length() < 9 ? null :  responseData.getPayer().getKpp();

            this.namePayer = responseData.getPayer().getName();

            this.sumPartExecKopeika = XmlLoader.formatInteger(responseData.getSumPartExecKopeika());

            this.account = responseData.getAccount();
        }
}
