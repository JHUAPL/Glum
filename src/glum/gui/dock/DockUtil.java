// Copyright (C) 2024 The Johns Hopkins University Applied Physics Laboratory LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package glum.gui.dock;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;

import bibliothek.gui.DockController;
import bibliothek.gui.DockFrontend;
import bibliothek.gui.DockStation;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.ScreenDockStation;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.action.DockAction;
import bibliothek.gui.dock.action.actions.SimpleButtonAction;
import bibliothek.gui.dock.station.screen.ScreenFullscreenAction;
import bibliothek.gui.dock.station.split.SplitFullScreenAction;

/**
 * Collection of utility methods useful for working dockables / dock stations.
 *
 * @author lopeznr1
 */
public class DockUtil
{
	/**
	 * Utility method to construct a SimpleButtonAction.
	 */
	public static SimpleButtonAction createAction(String aText, Icon aIcon, ActionListener aListener)
	{
		SimpleButtonAction retAction;

		retAction = new SimpleButtonAction();
		retAction.setText(aText);
		retAction.setIcon(aIcon);
		retAction.addActionListener(aListener);

		return retAction;
	}

	/**
	 * Utility method to return the appropriate DockAction associated with aStation which will cause a "full screen"
	 * action to be executed. Currently only object of type {@link SplitDockStation} and {@link ScreenDockStation} are
	 * supported.
	 */
	public static DockAction createFullScreenAction(DockStation aStation, DockController aController)
	{
		DockAction retAction;

		if (aStation instanceof SplitDockStation)
		{
			retAction = new SplitFullScreenAction((SplitDockStation) aStation);
			((SplitFullScreenAction) retAction).setController(aController);
		}
		else if (aStation instanceof ScreenDockStation)
		{
			retAction = new ScreenFullscreenAction((ScreenDockStation) aStation);
			((ScreenFullscreenAction) retAction).setController(aController);
		}
		else
		{
			throw new RuntimeException("Unsupported Dockable type: " + aStation);
		}

		return retAction;
	}

	/**
	 * Method to locate the first Dockable (contained within aStation) which is of type aClass
	 */
	public static <G1 extends Dockable> G1 findDockable(DockStation aStation, Class<G1> aClass)
	{
		Dockable evalDockable;

		for (int c1 = 0; c1 < aStation.getDockableCount(); c1++)
		{
			evalDockable = aStation.getDockable(c1);
			if (evalDockable instanceof DockStation)
				evalDockable = findDockable((DockStation) evalDockable, aClass);

			if (evalDockable.getClass() == aClass)
				return aClass.cast(evalDockable);
		}

		return null;
	}

	/**
	 * Method to locate the list of Dockables (contained within aFrontend) which is of one of the types in aClassArr. All
	 * child DockStations will be searched.
	 */
	public static List<Dockable> findDockableList(DockFrontend aFrontend, Class<?>... aClassArr)
	{
		Set<Class<?>> tmpClassS;
		List<Dockable> retItemL;

		// Transform the match class array to a set
		tmpClassS = new HashSet<>();
		for (Class<?> aClass : aClassArr)
			tmpClassS.add(aClass);

		retItemL = new ArrayList<>();
		for (DockStation aStation : aFrontend.getRoots())
			findDockableList(aStation, tmpClassS, retItemL);

		return retItemL;
	}

	/**
	 * Method to locate the list of Dockables (contained within aFrontend) which is of type of aClass. All child
	 * DockStations will be searched.
	 */
	public static <G1 extends Dockable> List<G1> findDockableList(DockFrontend aFrontend, Class<G1> aClass)
	{
		Set<Class<?>> tmpClassS;
		List<G1> retItemL;

		// Transform the match class array to a set
		tmpClassS = new HashSet<>();
		tmpClassS.add(aClass);

		retItemL = new ArrayList<>();
		for (DockStation aStation : aFrontend.getRoots())
			findDockableList(aStation, tmpClassS, retItemL);

		return retItemL;
	}

	/**
	 * Method to locate the list of Dockables (contained within aStation) which is of one of the types in aClassArr. All
	 * child DockStations will be searched.
	 */
	public static List<Dockable> findDockableList(DockStation aStation, Class<?>... aClassArr)
	{
		Set<Class<?>> tmpClassS;
		List<Dockable> retItemL;

		// Transform the match class array to a set
		tmpClassS = new HashSet<>();
		for (Class<?> aClass : aClassArr)
			tmpClassS.add(aClass);

		retItemL = new ArrayList<>();
		findDockableList(aStation, tmpClassS, retItemL);

		return retItemL;
	}

	/**
	 * Method to locate the list of Dockables (contained within aStation) which is of type of aClass. All child
	 * DockStations will be searched.
	 */
	public static <G1 extends Dockable> List<G1> findDockableList(DockStation aStation, Class<G1> aClass)
	{
		Set<Class<?>> tmpClassS;
		List<G1> retItemL;

		// Transform the match class array to a set
		tmpClassS = new HashSet<>();
		tmpClassS.add(aClass);

		retItemL = new ArrayList<>();
		findDockableList(aStation, tmpClassS, retItemL);

		return retItemL;
	}

	/**
	 * Helper method to remove all PlotGroupStations and ChartDockables
	 */
	public static <G1 extends Dockable> void removeAllDockablesOfType(DockFrontend aFrontend, Class<?>... aDockTypeArr)
	{
		List<Dockable> dockL;
		DockStation dockStation;

		// Gather all of the Dockables of interest
		dockL = DockUtil.findDockableList(aFrontend, aDockTypeArr);

		// Remove all of the Dockables
		for (Dockable aDock : dockL)
		{
			dockStation = aDock.getDockParent();
			if (dockStation != null)
				dockStation.drag(aDock);
		}
	}

	/**
	 * Recursive helper method to locate the list of Dockables (contained within aStation) which is of type aClass. The
	 * results will be stored in aItemList
	 */
	@SuppressWarnings("unchecked")
	private static <G1 extends Dockable> void findDockableList(DockStation aStation, Set<Class<?>> aClassS,
			List<G1> aItemL)
	{
		Dockable evalDockable;

		for (int c1 = 0; c1 < aStation.getDockableCount(); c1++)
		{
			evalDockable = aStation.getDockable(c1);
			for (Class<?> aClass : aClassS)
			{
				if (aClass.isAssignableFrom(evalDockable.getClass()) == true)
				{
					aItemL.add((G1) evalDockable);
					break;
				}
			}

			if (evalDockable instanceof DockStation)
				findDockableList((DockStation) evalDockable, aClassS, aItemL);
		}
	}

}
