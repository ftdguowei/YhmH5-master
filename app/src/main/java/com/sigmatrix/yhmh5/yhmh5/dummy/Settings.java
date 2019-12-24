package com.sigmatrix.yhmh5.yhmh5.dummy;

import android.content.SharedPreferences;

import java.util.UUID;

public class Settings {

  private int autofocusPeriod;
  private boolean sound;
  private boolean oneD;
  private boolean dm;
  private boolean qr;
  private boolean aztec;
  private boolean pdf417;
  private UUID uuid;

  public Settings(int autofocusPeriod, boolean sound,
      boolean oneD, boolean dm, boolean qr, boolean aztec, boolean pdf417,
      UUID uuid) {
    this.autofocusPeriod = autofocusPeriod;
    this.sound = sound;
    this.oneD = oneD;
    this.dm = dm;
    this.qr = qr;
    this.aztec = aztec;
    this.pdf417 = pdf417;
    this.uuid = uuid;
  }

  public Settings(SharedPreferences sharedPrefs) {
    autofocusPeriod = sharedPrefs.getInt("autofocusPeriod", 5);
    sound = sharedPrefs.getBoolean("sound", true);
    oneD = sharedPrefs.getBoolean("oneD", true);
    dm = sharedPrefs.getBoolean("dm", true);
    qr = sharedPrefs.getBoolean("qr", true);
    aztec = sharedPrefs.getBoolean("aztec", true);
    pdf417 = sharedPrefs.getBoolean("pdf417", true);
    uuid = UUID.fromString(sharedPrefs.getString("uuid", UUID.randomUUID().toString()));
  }

  public int getAutofocusPeriod() {
    return autofocusPeriod;
  }
  public void setAutofocusPeriod(int autofocusPeriod) {
    this.autofocusPeriod = autofocusPeriod;
  }

  public boolean isSound() {
    return sound;
  }
  public void setSound(boolean sound) {
    this.sound = sound;
  }

  public boolean isOneD() {
    return oneD;
  }

  public void setOneD(boolean oneD) {
    this.oneD = oneD;
  }

  public boolean isDm() {
    return dm;
  }

  public void setDm(boolean dm) {
    this.dm = dm;
  }

  public boolean isQr() {
    return qr;
  }

  public void setQr(boolean qr) {
    this.qr = qr;
  }

  public boolean isAztec() {
    return aztec;
  }

  public void setAztec(boolean aztec) {
    this.aztec = aztec;
  }

  public boolean isPDF417() {
    return pdf417;
  }

  public void setPDF417(boolean pdf417) {
    this.pdf417 = pdf417;
  }

  public UUID getUuid() {
    return uuid;
  }
  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public void save(SharedPreferences prefs) {
    if (prefs == null) {
      return;
    }
    SharedPreferences.Editor editor = prefs.edit();
    editor.putInt("autofocusPeriod", autofocusPeriod);
    editor.putBoolean("sound", sound);
    editor.putBoolean("oneD", oneD);
    editor.putBoolean("dm", dm);
    editor.putBoolean("qr", qr);
    editor.putBoolean("aztec", aztec);
    editor.putBoolean("pdf417", pdf417);
    editor.putString("uuid", uuid.toString());
    editor.commit();
  }
}
