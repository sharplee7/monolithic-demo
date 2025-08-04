package com.monolithicbank.b2bt.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class B2BTransferHistory implements Serializable{
    private String cstmId;
    private int seq;
    private String divCd;
    private String stsCd;
    private String dpstAcntNo;
    private String wthdAcntNo;
    private int wthdAcntSeq;
    private String sndMm;
    private String rcvMm;
    private String rcvCstmNm;
    private Long trnfAmt;
    private String trnfDtm;
}