package org.jproject.dto;

import org.jproject.domain.FlegFleh;

public class DtoLinkFileParameters {

    Integer flegId;
    Integer flehId;

    public DtoLinkFileParameters() {

    }

    public DtoLinkFileParameters(Integer flegId, Integer flehId) {
        this.flegId = flegId;
        this.flehId = flehId;
    }

    public static DtoLinkFileParameters of(FlegFleh.PK pk) {
        return new DtoLinkFileParameters(pk.getFlegFlegId(), pk.getFlehFlehId());
    }

    public Integer getFlegId() {
        return flegId;
    }

    public void setFlegId(Integer flegId) {
        this.flegId = flegId;
    }

    public Integer getFlehId() {
        return flehId;
    }

    public void setFlehId(Integer flehId) {
        this.flehId = flehId;
    }
}
