package org.jmodelica.ide.graphical.editparts;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Handle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.jmodelica.icons.Component;
import org.jmodelica.icons.Diagram;
import org.jmodelica.icons.coord.Extent;
import org.jmodelica.icons.coord.Point;
import org.jmodelica.icons.coord.Transformation;
import org.jmodelica.ide.graphical.commands.AddComponentCommand;
import org.jmodelica.ide.graphical.commands.MoveComponentCommand;
import org.jmodelica.ide.graphical.commands.ResizeComponentCommand;
import org.jmodelica.ide.graphical.graphics.RotatableLocator;
import org.jmodelica.ide.graphical.util.Converter;
import org.jmodelica.ide.graphical.util.Transform;


public class DiagramEditPart extends AbstractIconEditPart {

	public DiagramEditPart(Diagram diagram) {
		super(diagram);
	}

	protected IFigure createFigure() {
		IFigure f = new FreeformLayer();
		f.setBorder(new MarginBorder(3));
		f.setLayoutManager(new FreeformLayout());

		// Create the static router for the connection layer
		ConnectionLayer connLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
		connLayer.setConnectionRouter(new DiagramConnectionRouter());

		updateGrid();

		return f;
	}

	public void updateGrid() {
		Transform transform = getTransform();
		double[] realGrid = getModel().getLayer().getCoordinateSystem().getGrid();
		Point grid = transform.transform(new Point(realGrid[0], realGrid[0]));
		Point origin = transform.transform(new Point(0, 0));

		getViewer().setProperty(SnapToGrid.PROPERTY_GRID_ORIGIN, Converter.convert(origin));
		getViewer().setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension((int) Math.abs(grid.getX() - origin.getX()), (int) Math.abs(grid.getY() - origin.getY())));
	}

	protected void createEditPolicies() {
//		// disallows the removal of this edit part from its parent
//		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
		installEditPolicy("Snap Feedback", new SnapFeedbackPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy() {
			@Override
			public Command getCommand(Request request) {
				if (NativeDropRequest.ID.equals(request.getType())) {
					NativeDropRequest ndr = (NativeDropRequest) request;

					String className = (String) ndr.getData();
					String baseAutoName = className;
					int index = baseAutoName.lastIndexOf('.');
					if (index != -1)
						baseAutoName = baseAutoName.substring(index + 1);
					Set<String> usedNames = new HashSet<String>();
					for (Object part : getChildren()) {
						if (part instanceof ComponentEditPart)
							usedNames.add(((ComponentEditPart) part).getComponent().getComponentName());
					}
					int i = 1;
					String autoName = baseAutoName;
					while (usedNames.contains(autoName)) {
						i++;
						autoName = baseAutoName + i;
					}

					Component c = getModel().getFactory().createComponent(className, autoName);

					org.eclipse.draw2d.geometry.Point draw2dOrigin = ndr.getPoint().getCopy();
					getFigure().translateToRelative(draw2dOrigin);
					Point origin = Transform.yInverter.transform(getTransform().getInverseTransfrom().transform(Converter.convert(draw2dOrigin)));
					c.getPlacement().setIconTransformation(new Transformation(new Extent(new Point(-10, -10), new Point(10, 10)), origin));

					return new AddComponentCommand(getModel(), c);
				}
				return super.getCommand(request);
			}

			@Override
			public EditPart getTargetEditPart(Request request) {
				if (NativeDropRequest.ID.equals(request.getType()))
					return DiagramEditPart.this;
				return super.getTargetEditPart(request);
			}
		});
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicy() {

			protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, final Object constraint) {
				if (child instanceof ComponentEditPart && constraint instanceof Rectangle) {
					final ComponentEditPart iep = (ComponentEditPart) child;
					if (RequestConstants.REQ_RESIZE_CHILDREN.equals(request.getType())) {
						return new ResizeComponentCommand(iep.getComponent(), request) {
							@Override
							protected Extent calculateNewExtent() {
								Transform t = getTransform();
								t.translate(iep.getComponent().getPlacement().getTransformation().getOrigin().getX(), -iep.getComponent().getPlacement().getTransformation().getOrigin().getY());
								t = t.getInverseTransfrom();

								Extent newActualExtent = Transform.yInverter.transform(t.transform(Converter.convert((Rectangle) constraint)));
								Extent oldActualExtent = Transform.yInverter.transform(t.transform(Converter.convert(iep.getFigure().getBounds())));
								Extent oldExtent = iep.getComponent().getPlacement().getTransformation().getExtent();
								double scaleX = newActualExtent.getWidth() / oldActualExtent.getWidth();
								double scaleY = newActualExtent.getHeight() / oldActualExtent.getHeight();

								Point p1 = newActualExtent.getP1();
								p1 = new Point(
										p1.getX() + (oldExtent.getP1().getX() - oldActualExtent.getP1().getX()) * scaleX,
										p1.getY() + (oldExtent.getP1().getY() - oldActualExtent.getP1().getY()) * scaleY);
								Point p2 = newActualExtent.getP2();
								p2 = new Point(
										p2.getX() + (oldExtent.getP2().getX() - oldActualExtent.getP2().getX()) * scaleX,
										p2.getY() + (oldExtent.getP2().getY() - oldActualExtent.getP2().getY()) * scaleY);
								return new Extent(p1, p2);
							}

						};
					}
					if (RequestConstants.REQ_MOVE_CHILDREN.equals(request.getType())) {
						return new MoveComponentCommand(iep.getComponent(), request) {
							@Override
							protected Point calculateNewOrigin() {
								Transform t = getTransform().getInverseTransfrom();

								Point newActualOrigin = Transform.yInverter.transform(t.transform(Converter.convert(((Rectangle) constraint).getTopLeft())));
								Point oldActualOrigin = Transform.yInverter.transform(t.transform(Converter.convert((iep.getFigure().getBounds()).getTopLeft())));
								Point oldOrigin = iep.getComponent().getPlacement().getTransformation().getOrigin();

								double deltaX = newActualOrigin.getX() - oldActualOrigin.getX();
								double deltaY = newActualOrigin.getY() - oldActualOrigin.getY();

								return new Point(oldOrigin.getX() + deltaX, oldOrigin.getY() + deltaY);
							}
						};
					}
				}
				return super.createChangeConstraintCommand(request, child, constraint);
			}

			protected Command getCreateCommand(CreateRequest request) {
				return null;
			}
			
			@Override
			protected EditPolicy createChildEditPolicy(final EditPart child) {
				if (child instanceof AbstractIconEditPart) {
					return new ResizableEditPolicy() {
						
						@Override
						protected List<Handle> createSelectionHandles() {
							List<Handle> list = new ArrayList<Handle>();
							list.add(new org.jmodelica.ide.graphical.graphics.RotatableHandle((ComponentEditPart) child, RotatableLocator.TOP_LEFT));
							list.add(new org.jmodelica.ide.graphical.graphics.RotatableHandle((ComponentEditPart) child, RotatableLocator.TOP_RIGHT));
							list.add(new org.jmodelica.ide.graphical.graphics.RotatableHandle((ComponentEditPart) child, RotatableLocator.BOTTOM_LEFT));
							list.add(new org.jmodelica.ide.graphical.graphics.RotatableHandle((ComponentEditPart) child, RotatableLocator.BOTTOM_RIGHT));
							list.add(new org.jmodelica.ide.graphical.graphics.RotatableHandle((ComponentEditPart) child, RotatableLocator.TOP_CENTER));
							list.add(new org.jmodelica.ide.graphical.graphics.RotatableHandle((ComponentEditPart) child, RotatableLocator.MIDDLE_LEFT));
							list.add(new org.jmodelica.ide.graphical.graphics.RotatableHandle((ComponentEditPart) child, RotatableLocator.MIDDLE_RIGHT));
							list.add(new org.jmodelica.ide.graphical.graphics.RotatableHandle((ComponentEditPart) child, RotatableLocator.BOTTOM_CENTER));
							return list;
						}
					};
				} else {
					return super.createChildEditPolicy(child);
				}
			}
		});
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class key) {
		if (key == SnapToHelper.class) {
			List<SnapToHelper> helpers = new ArrayList<SnapToHelper>();
			if (Boolean.TRUE.equals(getViewer().getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED)))
				helpers.add(new SnapToGeometry(this));
			if (Boolean.TRUE.equals(getViewer().getProperty(SnapToGrid.PROPERTY_GRID_ENABLED)))
				helpers.add(new MySnapToGrid(this));

			if (helpers.size() == 0)
				return null;

			return new CompoundSnapToHelper(helpers.toArray(new SnapToHelper[helpers.size()]));
		}
		return super.getAdapter(key);
	}

	protected List<Object> getModelChildren() {
		List<Object> children = super.getModelChildren();
//		ArrayList<Object> newChildren = new ArrayList<Object>();
//		for (Object child : children) {
//			if (child instanceof Component) {
//				List<Point> points = new ArrayList<Point>(5);
//				points.add(new Point(((Component) child).getPlacement().getTransformation().getOrigin().getX() + ((Component) child).getPlacement().getTransformation().getExtent().getP1().getX(), ((Component) child).getPlacement().getTransformation().getOrigin().getY() + ((Component) child).getPlacement().getTransformation().getExtent().getP1().getY()));
//				points.add(new Point(((Component) child).getPlacement().getTransformation().getOrigin().getX() + ((Component) child).getPlacement().getTransformation().getExtent().getP2().getX(), ((Component) child).getPlacement().getTransformation().getOrigin().getY() + ((Component) child).getPlacement().getTransformation().getExtent().getP1().getY()));
//				points.add(new Point(((Component) child).getPlacement().getTransformation().getOrigin().getX() + ((Component) child).getPlacement().getTransformation().getExtent().getP2().getX(), ((Component) child).getPlacement().getTransformation().getOrigin().getY() + ((Component) child).getPlacement().getTransformation().getExtent().getP2().getY()));
//				points.add(new Point(((Component) child).getPlacement().getTransformation().getOrigin().getX() + ((Component) child).getPlacement().getTransformation().getExtent().getP1().getX(), ((Component) child).getPlacement().getTransformation().getOrigin().getY() + ((Component) child).getPlacement().getTransformation().getExtent().getP2().getY()));
//				points.add(new Point(((Component) child).getPlacement().getTransformation().getOrigin().getX() + ((Component) child).getPlacement().getTransformation().getExtent().getP1().getX(), ((Component) child).getPlacement().getTransformation().getOrigin().getY() + ((Component) child).getPlacement().getTransformation().getExtent().getP1().getY()));
//				newChildren.add(new Line(points));
//			}
//		}
//		newChildren.addAll(children);
//		children = newChildren;
		return children;
	}

	protected Transform calculateTransform() {
		Transform transform = new Transform();
		transform.translate(300, 300);
		transform.scale(3);
		return transform;
	}

	public Diagram getModel() {
		return (Diagram) super.getModel();
	}

}