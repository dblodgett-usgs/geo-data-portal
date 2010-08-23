package ucar.nc2.iosp.geotiff.epsg.csv;

import ucar.nc2.iosp.geotiff.epsg.UnitOfMeasure;

/**
 *
 * @author tkunicki
 */
public class UnitOfMeasureEntry implements CSVEntry, UnitOfMeasure {

    private int code;
    private String name;
    private String type;
    private int targetUnitOfMeasureCode;
    private double factorB;
    private double factorC;
    
    private UnitOfMeasure targetUnitOfMeasure;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public double getFactorB() {
        return factorB;
    }

    public void setFactorB(double factorB) {
        this.factorB = factorB;
    }

    public double getFactorC() {
        return factorC;
    }

    public void setFactorC(double factorC) {
        this.factorC = factorC;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTargetUnitOfMeasureCode(int targetUnitOfMeasureCode) {
        this.targetUnitOfMeasureCode = targetUnitOfMeasureCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public synchronized UnitOfMeasure getTargetUnitOfMeasure() {
        if (targetUnitOfMeasure == null) {
            targetUnitOfMeasure = EPSG.findUnitOfMeasureByCode(targetUnitOfMeasureCode);
        }
        return targetUnitOfMeasure;
    }

}
