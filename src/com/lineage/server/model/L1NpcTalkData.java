package com.lineage.server.model;

public class L1NpcTalkData {

    /* renamed from: ID */
    int f17ID;
    int NpcID;
    String caoticAction;
    String normalAction;
    String teleportURL;
    String teleportURLA;

    public String getNormalAction() {
        return this.normalAction;
    }

    public void setNormalAction(String normalAction2) {
        this.normalAction = normalAction2;
    }

    public String getCaoticAction() {
        return this.caoticAction;
    }

    public void setCaoticAction(String caoticAction2) {
        this.caoticAction = caoticAction2;
    }

    public String getTeleportURL() {
        return this.teleportURL;
    }

    public void setTeleportURL(String teleportURL2) {
        this.teleportURL = teleportURL2;
    }

    public String getTeleportURLA() {
        return this.teleportURLA;
    }

    public void setTeleportURLA(String teleportURLA2) {
        this.teleportURLA = teleportURLA2;
    }

    public int getID() {
        return this.f17ID;
    }

    public void setID(int id) {
        this.f17ID = id;
    }

    public int getNpcID() {
        return this.NpcID;
    }

    public void setNpcID(int npcID) {
        this.NpcID = npcID;
    }
}
