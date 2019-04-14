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

import java.util.* ;
import java.math.* ;


/** Factorials.
* @since 2006-06-25
* @since 2012-02-15 Storage of the values based on Ifactor, not BigInteger.
* @author Richard J. Mathar
*/
public class Factorial
{
        /** The list of all factorials as a vector.
        */
        static Vector<Ifactor> a = new Vector<Ifactor>() ;

        /** ctor().
        * Initialize the vector of the factorials with 0!=1 and 1!=1.
        */
        public Factorial()
        {
                if ( a.size() == 0 )
                {
                        a.add(Ifactor.ONE) ;
                        a.add(Ifactor.ONE) ;
                }
        } /* ctor */

        /** Compute the factorial of the non-negative integer.
        * @param n the argument to the factorial, non-negative.
        * @return the factorial of n.
        */
        public BigInteger at(int n)
        {
                /* extend the internal list if needed.
                */
                growto(n) ;
                return a.elementAt(n).n ;
        } /* at */

        /** Compute the factorial of the non-negative integer.
        * @param n the argument to the factorial, non-negative.
        * @return the factorial of n.
        */
        public Ifactor toIfactor(int n)
        {
                /* extend the internal list if needed.
                */
                growto(n) ;
                return a.elementAt(n) ;
        } /* at */

        /** Extend the internal table to cover up to n!
        * @param n The maximum factorial to be supported.
        * @since 2012-02-15
        */
        private void growto(int n)
        {
                /* extend the internal list if needed. Size to be 2 for n<=1, 3 for n<=2 etc.
                */
                while ( a.size() <=n )
                {
                        final int lastn = a.size()-1 ;
                        final Ifactor nextn = new Ifactor(lastn+1) ;
                        a.add(a.elementAt(lastn).multiply(nextn) ) ;
                }
        } /* growto */

} /* Factorial */
