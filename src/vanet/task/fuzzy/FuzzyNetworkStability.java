/*
Copyright (c) 2010 Che-Hung Lin

This file is part of VANET Mobility Generation Tool.

VANET Mobility Generation Tool is free software: you can redistribute it and/or
modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or (at your option)
any later version.

VANET Mobility Generation Tool is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details.

You should have received a copy of the GNU General Public License along with
VANET Mobility Generation Tool. If not, see <https://www.gnu.org/licenses/>.
*/
package vanet.task.fuzzy;

public class FuzzyNetworkStability {

	// FNS membership function
	float FNS_VL[] = {};
	float FNS_L[] = {};
	float FNS_M[] = {};
	float FNS_H[] = {};
	float FNS_VH[] = {};

	public float calculateFNS(float NC, float SS) {
		int i;
		float map_i, v_i, area, mount;
		float t001;

		float NC_L, NC_M, NC_H;
		float SS_VW, SS_W, SS_M, SS_S, SS_VS;
		float FNS[] = new float[11];

		/******** membership function of NC ********/
		NC_L = 0;
		if (NC >= 0 && NC <= 0.3)
			NC_L = (float) ((float) (0.3 - NC) / 0.3);

		NC_M = 0;
		if (NC >= 0.1 && NC <= 0.4)
			NC_L = (float) ((float) (0.4 - NC) / 0.3);
		if (NC >= 0.4 && NC <= 0.7)
			NC_L = (float) ((float) (0.7 - NC) / 0.3);

		NC_H = 0;
		if (NC >= 0 && NC <= 0.3)
			NC_L = (float) ((float) (0.3 - NC) / 0.3);

		/******** membership function of SS ********/
		SS_VW = 0;
		if (SS >= 0 && SS <= 0.3)
			SS_VW = (float) ((float) (0.3 - SS) / 0.3);

		SS_W = 0;
		if (SS >= 0.1 && SS <= 0.4)
			SS_W = (float) ((float) (0.4 - SS) / 0.3);
		if (SS >= 0.4 && SS <= 0.7)
			SS_W = (float) ((float) (0.7 - SS) / 0.3);

		SS_M = 0;
		if (SS >= 0 && SS <= 0.3)
			SS_M = (float) ((float) (0.3 - SS) / 0.3);

		SS_S = 0;
		if (SS >= 0 && SS <= 0.3)
			SS_S = (float) ((float) (0.3 - SS) / 0.3);

		SS_VS = 0;
		if (SS >= 0 && SS <= 0.3)
			SS_VS = (float) ((float) (0.3 - SS) / 0.3);

		/******** Rule 1 ********/
		/* NC is Low and SS is Very weak */
		t001 = Math.min(NC_L, SS_VW);
		System.out.println("Rule 1: " + t001);

		/* FNS = Very Low */
		if (t001 != 0) {
			for (i = 0; i < FNS.length; i++) {
				map_i = (float) FNS_VL[i];
				FNS[i] = Math.max(FNS[i], Math.min(t001, map_i));
			}
		}

		/******** Rule 2 ********/
		/* NC is Low and SS is weak */
		t001 = Math.min(NC_L, SS_W);
		System.out.println("Rule 2: " + t001);

		/* FNS = Very Low */
		if (t001 != 0) {
			for (i = 0; i < FNS.length; i++) {
				map_i = (float) FNS_VL[i];
				FNS[i] = Math.max(FNS[i], Math.min(t001, map_i));
			}
		}

		/******** Rule 3 ********/
		/* NC is Low and SS is Medium */
		t001 = Math.min(NC_L, SS_M);
		System.out.println("Rule 3: " + t001);

		/* FNS = Low */
		if (t001 != 0) {
			for (i = 0; i < FNS.length; i++) {
				map_i = (float) FNS_L[i];
				FNS[i] = Math.max(FNS[i], Math.min(t001, map_i));
			}
		}

		/******** Rule 4 ********/
		/* NC is Low and SS is Strong */
		t001 = Math.min(NC_L, SS_S);
		System.out.println("Rule 4: " + t001);

		/* FNS = Medium */
		if (t001 != 0) {
			for (i = 0; i < FNS.length; i++) {
				map_i = (float) FNS_M[i];
				FNS[i] = Math.max(FNS[i], Math.min(t001, map_i));
			}
		}

		/******** Rule 5 ********/
		/* NC is Low and SS is Very Strong */
		t001 = Math.min(NC_L, SS_VS);
		System.out.println("Rule 5: " + t001);

		/* FNS = High */
		if (t001 != 0) {
			for (i = 0; i < FNS.length; i++) {
				map_i = (float) FNS_H[i];
				FNS[i] = Math.max(FNS[i], Math.min(t001, map_i));
			}
		}

		/******** Rule 6 ********/
		/* NC is Medium and SS is Very Weak */
		t001 = Math.min(NC_M, SS_VW);
		System.out.println("Rule 6: " + t001);

		/* FNS = Very Low */
		if (t001 != 0) {
			for (i = 0; i < FNS.length; i++) {
				map_i = (float) FNS_VL[i];
				FNS[i] = Math.max(FNS[i], Math.min(t001, map_i));
			}
		}

		/******** Rule 7 ********/
		/* NC is Medium and SS is Weak */
		t001 = Math.min(NC_M, SS_W);
		System.out.println("Rule 7: " + t001);

		/* FNS = Low */
		if (t001 != 0) {
			for (i = 0; i < FNS.length; i++) {
				map_i = (float) FNS_L[i];
				FNS[i] = Math.max(FNS[i], Math.min(t001, map_i));
			}
		}

		/******** Rule 8 ********/
		/* NC is Medium and SS is Medium */
		t001 = Math.min(NC_M, SS_M);
		System.out.println("Rule 8: " + t001);

		/* FNS = Medium */
		if (t001 != 0) {
			for (i = 0; i < FNS.length; i++) {
				map_i = (float) FNS_M[i];
				FNS[i] = Math.max(FNS[i], Math.min(t001, map_i));
			}
		}

		/******** Rule 9 ********/
		/* NC is Medium and SS is Strong */
		t001 = Math.min(NC_M, SS_S);
		System.out.println("Rule 9: " + t001);

		/* FNS = High */
		if (t001 != 0) {
			for (i = 0; i < FNS.length; i++) {
				map_i = (float) FNS_H[i];
				FNS[i] = Math.max(FNS[i], Math.min(t001, map_i));
			}
		}

		/******** Rule 10 ********/
		/* NC is Medium and SS is Very Strong */
		t001 = Math.min(NC_M, SS_VS);
		System.out.println("Rule 10: " + t001);

		/* FNS = Very High */
		if (t001 != 0) {
			for (i = 0; i < FNS.length; i++) {
				map_i = (float) FNS_VH[i];
				FNS[i] = Math.max(FNS[i], Math.min(t001, map_i));
			}
		}

		/******** Rule FNS.length ********/
		/* NC is High and SS is Very Weak */
		t001 = Math.min(NC_H, SS_VW);
		System.out.println("Rule FNS.length: " + t001);

		/* FNS = Medium */
		if (t001 != 0) {
			for (i = 0; i < FNS.length; i++) {
				map_i = (float) FNS_M[i];
				FNS[i] = Math.max(FNS[i], Math.min(t001, map_i));
			}
		}

		/******** Rule 12 ********/
		/* NC is High and SS is Weak */
		t001 = Math.min(NC_H, SS_W);
		System.out.println("Rule 12: " + t001);

		/* FNS = Medium */
		if (t001 != 0) {
			for (i = 0; i < FNS.length; i++) {
				map_i = (float) FNS_M[i];
				FNS[i] = Math.max(FNS[i], Math.min(t001, map_i));
			}
		}

		/******** Rule 13 ********/
		/* NC is High and SS is Medium */
		t001 = Math.min(NC_H, SS_M);
		System.out.println("Rule 13: " + t001);

		/* FNS = High */
		if (t001 != 0) {
			for (i = 0; i < FNS.length; i++) {
				map_i = (float) FNS_H[i];
				FNS[i] = Math.max(FNS[i], Math.min(t001, map_i));
			}
		}

		/******** Rule 14 ********/
		/* NC is High and SS is Strong */
		t001 = Math.min(NC_H, SS_S);
		System.out.println("Rule 14: " + t001);

		/* FNS = Very High */
		if (t001 != 0) {
			for (i = 0; i < FNS.length; i++) {
				map_i = (float) FNS_VH[i];
				FNS[i] = Math.max(FNS[i], Math.min(t001, map_i));
			}
		}

		/******** Rule 15 ********/
		/* NC is High and SS is Very Strong */
		t001 = Math.min(NC_H, SS_VS);
		System.out.println("Rule 15: " + t001);

		/* FNS = Very High */
		if (t001 != 0) {
			for (i = 0; i < FNS.length; i++) {
				map_i = (float) FNS_VH[i];
				FNS[i] = Math.max(FNS[i], Math.min(t001, map_i));
			}
		}

		/** Calculate the output value **/

		area = (float) 0.0;
		mount = (float) 0.0;
		for (i = 0; i < FNS.length; i++) {
			map_i = FNS[i];
			System.out.println("i=" + 1 + " map_i=" + map_i);
			v_i = (float) (-0.5 + (float) i);
			area += map_i;
			mount += map_i * v_i;
		}
		if (area == 0)
			return (float) 5.0;
		else
			return (float) (mount / area);

	}
}
