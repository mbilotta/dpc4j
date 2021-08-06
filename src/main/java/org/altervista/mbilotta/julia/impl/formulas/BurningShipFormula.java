package org.altervista.mbilotta.julia.impl.formulas;

import org.altervista.mbilotta.julia.Author;
import org.altervista.mbilotta.julia.Decimal;
import org.altervista.mbilotta.julia.NumberFactory;
import org.altervista.mbilotta.julia.impl.AbstractFormula;
import org.altervista.mbilotta.julia.math.Complex;
import org.altervista.mbilotta.julia.math.Real;
import org.altervista.mbilotta.julia.program.parsers.RealParameter;

@Author(name = "Maurizio Bilotta", contact = "mailto:maurizeio@yahoo.it")
public class BurningShipFormula extends AbstractFormula<BurningShipFormula> {

    private Real bailout;

    private Real zero;
    private Real bailout2;

	public BurningShipFormula() {
		bailout = new Decimal(20);
	}

	public BurningShipFormula(Real bailout) {
		this.bailout = bailout;
	}

    @Override
    public boolean bailoutOccured() {
        return getZ().absSquared().gt(bailout2);
    }

    @Override
    public void initJuliaIteration(Complex z) {
        setZ(z);
    }

    @Override
    public void initMandelbrotIteration(Complex c) {
        setZ(zero);
        setC(c);
    }

    @Override
    public void iterate() {
        Complex z = getZ();
        Complex zNext = z.re().abs().minus(z.im().abs().i()).square().plus(getC());
        setZ(zNext);
    }

    @Override
    public BurningShipFormula newInstance() {
        return new BurningShipFormula(bailout);
    }

	public Real getBailout() {
		return bailout;
	}

    @RealParameter.Min(value = "0", inclusive = false)
    public void setBailout(Real bailout) {
        this.bailout = bailout;
    }

	@Override
	public void cacheConstants(NumberFactory numberFactory) {
		zero = numberFactory.zero();
		bailout2 = bailout.square();
	}
}