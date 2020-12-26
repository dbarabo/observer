package ru.barabo.observer.config.task.p311.v512;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("СвНП")
public final class SvNp {

    @XStreamAlias("КодЛица")
    private Integer codeFace;

    @XStreamAlias("СвидНУ")
    private String svidNU;

    @XStreamAlias("НПРО")
    private NpRo npRO;

    @XStreamAlias("НПИП")
    private NpIP npIP;

    @XStreamAlias("НПИО")
    private NpIO npIO;

    @XStreamAlias("НЕТИНН")
    private NoInn noInn;

    // for Physic
    @XStreamAlias("НПФЛ")
    private NpFl npFl;

    public SvNp(Integer codeFace, String svidNU, NpRo npRO) {

        this.codeFace = codeFace;
        this.svidNU = svidNU;

        this.npRO = npRO;
    }

    public SvNp(Integer codeFace, String svidNU, NpIP npIP) {

        this.codeFace = codeFace;
        this.svidNU = svidNU;

        this.npIP = npIP;
    }

    public SvNp(Integer codeFace, String svidNU, NpIO npIO) {

        this.codeFace = codeFace;
        this.svidNU = svidNU;

        this.npIO = npIO;
    }

    public SvNp(Integer codeFace, NpFl npFl) {

        this.codeFace = codeFace;
        this.npFl = npFl;
    }
}
