package math.types;

import java.util.Objects;

public class ComplexDouble {
    private double re;
    private double im;


    public ComplexDouble() {
    }

    public ComplexDouble(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double real() {
        return this.re;
    }

    public double imagine() {
        return this.im;
    }

    public ComplexDouble set(double re, double im) {
        this.re = re;
        this.im = im;
        return this;
    }

    public ComplexDouble set(ComplexDouble complex) {
        this.re = complex.re;
        this.im = complex.im;
        return this;
    }

    public ComplexDouble sum(ComplexDouble other) {
        double re = this.re + other.re;
        double im = this.im + other.im;
        return new ComplexDouble(re, im);
    }

    public ComplexDouble sub(ComplexDouble other) {
        double re = this.re - other.re;
        double im = this.im - other.im;
        return new ComplexDouble(re, im);
    }

    public ComplexDouble mul(ComplexDouble other) {
        double re = this.re * other.re - this.im * other.im;
        double im = this.re * other.im + this.im * other.re;
        return new ComplexDouble(re, im);
    }

    public ComplexDouble div(ComplexDouble other) {
        double divider = other.re * other.re + other.im * other.im;
        double re = (this.re * other.re + this.im * other.im) / divider;
        double im = (this.im * other.re - this.re * other.im) / divider;
        return new ComplexDouble(re, im);
    }

    public ComplexDouble pow(int power) {
        double poweredLength = Math.pow(this.radius(), power);
        double angle = this.angle();
        double re = poweredLength * Math.cos(power * angle);
        double im = poweredLength * Math.sin(power * angle);
        return new ComplexDouble(re, im);
    }

    public ComplexDouble conjugate() {
        return new ComplexDouble(re, -im);
    }

    public double radius() {
        return Math.sqrt(this.re * this.re + this.im * this.im);
    }

    public double angle() {
        return Math.atan(this.im / this.re);
    }

    public static ComplexDouble exp(ComplexDouble complex) {
        return new ComplexDouble(1.0d, complex.im).mul(new ComplexDouble(Math.exp(complex.re), 0.0d));
    }

    @Override
    public String toString() {
        return String.format("re=%f,\t im=%f", re, im);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexDouble that = (ComplexDouble) o;
        return Double.compare(that.re, re) == 0 &&
                Double.compare(that.im, im) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(re, im);
    }
}
