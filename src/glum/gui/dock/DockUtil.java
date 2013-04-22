package glum.gui.dock;

import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

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

public class DockUtil
{
	/**
	 * Utility method to construct a SimpleButtonAction.
	 */
	public static SimpleButtonAction createAction(String aText, Icon aIcon, ActionListener aListener)
	{
		SimpleButtonAction aAction;

		aAction = new SimpleButtonAction();
		aAction.setText(aText);
		aAction.setIcon(aIcon);
		aAction.addActionListener(aListener);

		return aAction;
	}

	/**
	 * Utility method to return the appropriate DockAction associated with aStation which will cause a "full screen"
	 * action to be executed. Currently only object of type {@link SplitDockStation} and {@link ScreenDockStation} are
	 * supported.
	 */
	public static DockAction createFullScreenAction(DockStation aStation, DockController aController)
	{
		DockAction fullScreenAction;

		if (aStation instanceof SplitDockStation)
		{
			fullScreenAction = new SplitFullScreenAction((SplitDockStation)aStation);
			((SplitFullScreenAction)fullScreenAction).setController(aController);
		}
		else if (aStation instanceof ScreenDockStation)
		{
			fullScreenAction = new ScreenFullscreenAction((ScreenDockStation)aStation);
			((ScreenFullscreenAction)fullScreenAction).setController(aController);
		}
		else
		{
			throw new RuntimeException("Unsupported Dockable type: " + aStation);
		}

		return fullScreenAction;
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
				evalDockable = findDockable((DockStation)evalDockable, aClass);

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
		Set<Class<?>> classSet;
		List<Dockable> itemList;

		// Transform the match class array to a set
		classSet = Sets.newHashSet();
		for (Class<?> aClass : aClassArr)
			classSet.add(aClass);

		itemList = Lists.newLinkedList();
		for (DockStation aStation : aFrontend.getRoots())
			findDockableList(aStation, classSet, itemList);

		return itemList;
	}

	/**
	 * Method to locate the list of Dockables (contained within aFrontend) which is of type of aClass. All child
	 * DockStations will be searched.
	 */
	public static <G1 extends Dockable> List<G1> findDockableList(DockFrontend aFrontend, Class<G1> aClass)
	{
		Set<Class<?>> classSet;
		List<G1> itemList;

		// Transform the match class array to a set
		classSet = Sets.newHashSet();
		classSet.add(aClass);

		itemList = Lists.newLinkedList();
		for (DockStation aStation : aFrontend.getRoots())
			findDockableList(aStation, classSet, itemList);

		return itemList;
	}

	/**
	 * Method to locate the list of Dockables (contained within aStation) which is of one of the types in aClassArr. All
	 * child DockStations will be searched.
	 */
	public static List<Dockable> findDockableList(DockStation aStation, Class<?>... aClassArr)
	{
		Set<Class<?>> classSet;
		List<Dockable> itemList;

		// Transform the match class array to a set
		classSet = Sets.newHashSet();
		for (Class<?> aClass : aClassArr)
			classSet.add(aClass);

		itemList = Lists.newLinkedList();
		findDockableList(aStation, classSet, itemList);

		return itemList;
	}

	/**
	 * Method to locate the list of Dockables (contained within aStation) which is of type of aClass. All child
	 * DockStations will be searched.
	 */
	public static <G1 extends Dockable> List<G1> findDockableList(DockStation aStation, Class<G1> aClass)
	{
		Set<Class<?>> classSet;
		List<G1> itemList;

		// Transform the match class array to a set
		classSet = Sets.newHashSet();
		classSet.add(aClass);

		itemList = Lists.newLinkedList();
		findDockableList(aStation, classSet, itemList);

		return itemList;
	}

	/**
	 * Helper method to remove all PlotGroupStations and ChartDockables
	 */
	public static <G1 extends Dockable> void removeAllDockablesOfType(DockFrontend aFrontend, Class<?>... dockTypeArr)
	{
		List<Dockable> dockList;
		DockStation dockStation;

		// Gather all of the Dockables of interest
		dockList = DockUtil.findDockableList(aFrontend, dockTypeArr);

		// Remove all of the Dockables
		for (Dockable aDock : dockList)
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
	private static <G1 extends Dockable> void findDockableList(DockStation aStation, Set<Class<?>> aClassSet, List<G1> aItemList)
	{
		Dockable evalDockable;

		for (int c1 = 0; c1 < aStation.getDockableCount(); c1++)
		{
			evalDockable = aStation.getDockable(c1);
			for (Class<?> aClass : aClassSet)
			{
				if (aClass.isAssignableFrom(evalDockable.getClass()) == true)
				{
					aItemList.add((G1)evalDockable);
					break;
				}
			}

			if (evalDockable instanceof DockStation)
				findDockableList((DockStation)evalDockable, aClassSet, aItemList);
		}
	}

}
