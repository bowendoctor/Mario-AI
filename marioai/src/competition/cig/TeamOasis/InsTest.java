package competition.cig.TeamOasis;

public class InsTest {
    //a data container with  7 attributes
    private String e;
    private String d;
    private String x;
    private String c;
    private String target;
    private String s;

    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
    }

    private String f;

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }


    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InsTest)) return false;

        InsTest insTest = (InsTest) o;

        if (c != null ? !c.equals(insTest.c) : insTest.c != null) return false;
        if (d != null ? !d.equals(insTest.d) : insTest.d != null) return false;
        if (e != null ? !e.equals(insTest.e) : insTest.e != null) return false;
        if (target != null ? !target.equals(insTest.target) : insTest.target != null) return false;
        if (x != null ? !x.equals(insTest.x) : insTest.x != null) return false;
        if (s != null ? !s.equals(insTest.s) : insTest.s != null) return false;
        if (f != null ? !f.equals(insTest.f) : insTest.f != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = e != null ? e.hashCode() : 0;
        result = 31 * result + (d != null ? d.hashCode() : 0);
        result = 31 * result + (x != null ? x.hashCode() : 0);
        result = 31 * result + (c != null ? c.hashCode() : 0);
        result = 31 * result + (target != null ? target.hashCode() : 0);

        result = 31 * result + (s != null ? s.hashCode() : 0);
        result = 31 * result + (f != null ? f.hashCode() : 0);
        return result;
    }


    public String Attribute(int i){
        switch(i){
            case 0:
                return e;
            case 1:
                return d;
            case 2:
                return x;
            case 3:
                return c;
            case 4:
                return s;
            case 5:
                return f;
            default:
                return "";
        }
    }

    public void assignFeature(int i,String str){
        switch (i){
            case 0:
                setE(str);
                break;
            case 1:
                setD(str);
                break;
            case 2:
                setX(str);
                break;
            case 3:
                setC(str);
                break;
            case 4:
                setS(str);
                break;
            case 5:
                setF(str);
                break;
            default:
                break;
        }
    }
    public int[] generateFeature() {
        int[] feature = new int[6];
        for(int i = 0;i < feature.length;i ++){
            switch (i){
                case 0:
                    feature[i] = Integer.valueOf(getE());
                    break;
                case 1:
                    feature[i] = Integer.valueOf(getD());
                    break;
                case 2:
                    feature[i] = Integer.valueOf(getX());
                    break;
                case 3:
                    feature[i] = Integer.valueOf(getC());
                    break;
                case 4:
                    feature[i] = Integer.valueOf(getS());
                    break;
                case 5:
                    feature[i] = Integer.valueOf(getF());
                    break;
                default:
                    break;
            }
        }
        return feature;
    }
    public double[] generateFeature2() {
        double[] feature = new double[6];
        for(int i = 0;i < feature.length;i ++){
            switch (i){
                case 0:
                    feature[i] = Integer.valueOf(getE());
                    break;
                case 1:
                    feature[i] = Integer.valueOf(getD());
                    break;
                case 2:
                    feature[i] = Integer.valueOf(getX());
                    break;
                case 3:
                    feature[i] = Integer.valueOf(getC());
                    break;
                case 4:
                    feature[i] = Integer.valueOf(getS());
                    break;
                case 5:
                    feature[i] = Integer.valueOf(getF());
                    break;
                default:
                    break;
            }
        }
        return feature;
    }
}
