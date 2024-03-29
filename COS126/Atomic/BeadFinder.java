import java.util.*;
public class BeadFinder {
  private Blob[] blobs;
  private int[][] isChecked;
  private Picture input;
  private Double threshhold;
  
  //  finds all blobs in the specified picture using luminance threshold tau
  public BeadFinder(Picture picture, double tau) { //queue
    int blobCount = 0;
    isChecked = new int[640][480]; //[row][column], 1 = checked
    input = picture;
    threshhold = tau;
    ArrayList<Blob> blobAL = new ArrayList<Blob>();
    for (int j = 0; j < 640; j++) { // i represents height
      for (int i = 0; i < 480; i++) {
        if (isChecked[j][i] != 1) {
          if (Luminance.lum(picture.get(j, i)) >= tau) {
            Blob x = new Blob();
            blobAL.add(x);
            blobCount++;
            dfs(j, i, x);
          }
        }
      }
    }
    blobs = new Blob[blobCount];
    blobs = blobAL.toArray(blobs);
  }
  //recursive dfs helper function
  private void dfs (int x, int y, Blob blob) {
    //edge case
    if (x < 0 || y < 0 || x > 639 || y > 479) {
      return;
    }
    
    if (isChecked[x][y] == 1) {
      return;
    } else {
      isChecked[x][y] = 1;
    }
    
    
    
    if (Luminance.lum(input.get(x, y)) >= threshhold) {
      blob.add(x, y);
    } else {
      return;
    }
    
    //8 pixels to dfs, can be 4.
    dfs(x + 1, y, blob);
    dfs(x + 1, y + 1, blob);
    dfs(x + 1, y - 1, blob);
    dfs(x - 1, y, blob);
    dfs(x, y + 1, blob);
    dfs(x, y - 1, blob);
    dfs(x - 1, y - 1, blob);
    dfs(x - 1, y + 1, blob);
    
  }
  
  //  returns all beads (blobs with >= min pixels)
  public Blob[] getBeads(int min) {
    int beadCount = 0;
    Blob[] beads = new Blob[blobs.length];
    for (int i = 0; i < blobs.length; i++) {
      if (blobs[i].mass() >= min) {
        beads[beadCount] = blobs[i];
        beadCount++;
      }
    }
    Blob[] realBeads = new Blob[beadCount];
    for (int i = 0; i < beadCount; i++) {
      realBeads[i] = beads[i];
    }
    return realBeads;
  }
  
  //  test client, as described below
  public static void main(String[] args) {
    int min = Integer.parseInt(args[0]);
    float tau = Float.parseFloat(args[1]);
    String filename = args[2];
    Picture input = new Picture(filename);
    
    BeadFinder test = new BeadFinder(input, tau);
    Blob[] output = test.getBeads(min);
    for (int i = 0; i < output.length; i++) {
      System.out.println(output[i]);
    }
  }
  
}