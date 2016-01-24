package competition.cig.TeamOasis;

public class instant {
    //a data container with  16 attributes
    private String target;
    private String q; // 0
    private String w; // 1
    private String e; // 2
    private String a; // 3
    private String d; // 4
    private String z; // 5
    private String x; // 6
    private String c; // 7

    private String s;

    private double s1;
    private double s2;
    private double s3;
    private double s4;
    private double s5;
    private double s6;
    private double s7;

    public double getS1() {
        return s1;
    }

    public void setS1(double s1) {
        this.s1 = s1;
    }

    public double getS2() {
        return s2;
    }

    public void setS2(double s2) {
        this.s2 = s2;
    }

    public double getS3() {
        return s3;
    }

    public void setS3(double s3) {
        this.s3 = s3;
    }

    public double getS4() {
        return s4;
    }

    public void setS4(double s4) {
        this.s4 = s4;
    }

    public double getS5() {
        return s5;
    }

    public void setS5(double s5) {
        this.s5 = s5;
    }

    public double getS6() {
        return s6;
    }

    public void setS6(double s6) {
        this.s6 = s6;
    }

    public double getS7() {
        return s7;
    }

    public void setS7(double s7) {
        this.s7 = s7;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
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

    public void assignFeature(int i,String str){
        switch (i){
            case 0:
                setQ(str);
                break;
            case 1:
                setW(str);
                break;
            case 2:
                setE(str);
                break;
            case 3:
                setA(str);
                break;
            case 4:
                setD(str);
                break;
            case 5:
                setZ(str);
                break;
            case 6:
                setX(str);
                break;
            case 7:
                setC(str);
                break;
            case 8:
                setS(str);
                break;
            default:
                break;
        }
    }
    public void assignDouble(int i,double str){
        switch (i){
            case 9:
                setS1(str);
                break;
            case 10:
                setS2(str);
                break;
            case 11:
                setS3(str);
                break;
            case 12:
                setS4(str);
                break;
            case 13:
                setS5(str);
                break;
            case 14:
                setS6(str);
                break;
            case 15:
                setS7(str);
                break;
        }
    }
    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public double[] generateFeature() {
        double[] feature = new double[16];
        for(int i = 0;i < feature.length;i ++){
            switch (i){
                case 0:
                    feature[i] = Integer.valueOf(getQ());
                    break;
                case 1:
                    feature[i] = Integer.valueOf(getW());
                    break;
                case 2:
                    feature[i] = Integer.valueOf(getE());
                    break;
                case 3:
                    feature[i] = Integer.valueOf(getA());
                    break;
                case 4:
                    feature[i] = Integer.valueOf(getD());
                    break;
                case 5:
                    feature[i] = Integer.valueOf(getZ());
                    break;
                case 6:
                    feature[i] = Integer.valueOf(getX());
                    break;
                case 7:
                    feature[i] = Integer.valueOf(getC());
                    break;
                case 8:
                    feature[i] = Integer.valueOf(getS());
                    break;
                case 9:
                    feature[i] = getS1();
                    break;
                case 10:
                    feature[i] = getS2();
                    break;
                case 11:
                    feature[i] = getS3();
                    break;
                case 12:
                    feature[i] = getS4();
                    break;
                case 13:
                    feature[i] = getS5();
                    break;
                case 14:
                    feature[i] = getS6();
                    break;
                case 15:
                    feature[i] = getS7();
                    break;
                default:
                    break;
            }
        }
        return feature;
    }
    public boolean isEqual(instant other) {
        if(this.s1==other.s1&&this.s2==other.s2&&this.s3==other.s3&&this.s4==other.s4&&this.s5==other.s5&&this.s6==other.s6&&this.s7==other.s7&&this.q.equals(other.q)&&this.w.equals(other.w)&&this.e.equals(other.e)&&this.a.equals(other.a)&&this.s.equals(other.s)&&this.d.equals(other.d)&&this.z.equals(other.z)&&this.x.equals(other.x)&&this.c.equals(other.c)&&this.target.equals(other.target))
            return true;
        else
            return false;
    }

    public boolean isDiff(instant other) {
        if(this.s1==other.s1&&this.s2==other.s2&&this.s3==other.s3&&this.s4==other.s4&&this.s5==other.s5&&this.s6==other.s6&&this.s7==other.s7&&this.q.equals(other.q)&&this.w.equals(other.w)&&this.e.equals(other.e)&&this.a.equals(other.a)&&this.s.equals(other.s)&&this.d.equals(other.d)&&this.z.equals(other.z)&&this.x.equals(other.x)&&this.c.equals(other.c)&&!this.target.equals(other.target))
            return true;
        else
            return false;
    }
}
