/*
 * Copied from org.nevec.rjm (Java library for multi-precision evaluation of basic functions).
 * See <http://arxiv.org/abs/0908.3030>.
 * 
 * Copyright (C) Richard J. Mathar <mathar@mpia.de>, licensed under the LGPL v3.0.
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.altervista.mbilotta.rjm ;

import java.math.BigInteger;
import java.util.Vector;

/** Euler numbers
* <a href="http://oeis.org/A000364">A000364</a> in the OEIS.
* @since 2008-10-30
* @author Richard J. Mathar
*/
public class Euler
{
        /*
        * The list of all Euler numbers as a vector, n=0,2,4,....
        */
        static protected Vector<BigInteger> a = new Vector<BigInteger>() ;

        /** Ctor(). Fill the hash list initially with E_0 to E_3.
        * @author Richard J. Mathar
        */
        public Euler()
        {
                if ( a.size() == 0 )
                {
                        a.add(BigInteger.ONE) ;
                        a.add(BigInteger.ONE) ;
                        a.add(new BigInteger("5")) ;
                        a.add(new BigInteger("61")) ;
                }
        }

        /** Compute a coefficient in the internal table.
        * @param n the zero-based index of the coefficient. n=0 for the E_0 term. 
        * @author Richard J. Mathar
        */
        protected void set(final int n)
        {
                while ( n >= a.size())
                {
                        BigInteger val = BigInteger.ZERO ;
                        boolean sigPos = true; 
                        int thisn = a.size() ;
                        for(int i= thisn-1 ; i > 0 ; i--)
                        {
                                BigInteger f = new BigInteger(""+ a.elementAt(i).toString() ) ;
                                f = f.multiply( BigIntegerMath.binomial(2*thisn,2*i) );
                                if ( sigPos )
                                        val = val.add(f) ;
                                else
                                        val = val.subtract(f) ;
                                sigPos = ! sigPos ;
                        }
                        if ( thisn % 2 ==0 )
                                val = val.subtract(BigInteger.ONE) ;
                        else
                                val = val.add(BigInteger.ONE) ;
                        a.add(val) ;
                }
        }

        /** The Euler number at the index provided.
        * @param n the index, non-negative.
        * @return the E_0=E_1=1 , E_2=5, E_3=61 etc
        * @author Richard J. Mathar
        */
        public BigInteger at(int n)
        {
                set(n) ;
                return(a.elementAt(n)) ;
        }

} /* Euler */
