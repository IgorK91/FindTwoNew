package ru.korelyakov.findtwonew.http;

import java.io.Serializable;

public class ResponseAnswer implements Serializable {

   //  private boolean active;
  //   private int level;
     private String go;
   //  private String max;
     private String tool;

    public ResponseAnswer() {
    }

  //  public boolean isActive() {
 //       return active;
  //  }

  //  public void setActive(boolean active) {
  //      this.active = active;
  //  }

  //  public int getLevel() {
  //      return level;
 //   }

 //   public void setLevel(int level) {
 //       this.level = level;
 //   }

    public String getGo() {
        return go;
    }

    public void setGo(String go) {
        this.go = go;
    }

  //  public String getMax() {
   //     return max;
  //  }

  //  public void setMax(String max) {
  //      this.max = max;
  //  }

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }
}
