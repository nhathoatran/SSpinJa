/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scheduling.dsl.CheckPoint;
import scheduling.dsl.DslPackage;
import scheduling.dsl.PointID;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Check Point</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.impl.CheckPointImpl#getPointid <em>Pointid</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CheckPointImpl extends StatementImpl implements CheckPoint
{
  /**
   * The cached value of the '{@link #getPointid() <em>Pointid</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPointid()
   * @generated
   * @ordered
   */
  protected PointID pointid;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected CheckPointImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return DslPackage.eINSTANCE.getCheckPoint();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public PointID getPointid()
  {
    return pointid;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetPointid(PointID newPointid, NotificationChain msgs)
  {
    PointID oldPointid = pointid;
    pointid = newPointid;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.CHECK_POINT__POINTID, oldPointid, newPointid);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setPointid(PointID newPointid)
  {
    if (newPointid != pointid)
    {
      NotificationChain msgs = null;
      if (pointid != null)
        msgs = ((InternalEObject)pointid).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.CHECK_POINT__POINTID, null, msgs);
      if (newPointid != null)
        msgs = ((InternalEObject)newPointid).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.CHECK_POINT__POINTID, null, msgs);
      msgs = basicSetPointid(newPointid, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.CHECK_POINT__POINTID, newPointid, newPointid));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case DslPackage.CHECK_POINT__POINTID:
        return basicSetPointid(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case DslPackage.CHECK_POINT__POINTID:
        return getPointid();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case DslPackage.CHECK_POINT__POINTID:
        setPointid((PointID)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case DslPackage.CHECK_POINT__POINTID:
        setPointid((PointID)null);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case DslPackage.CHECK_POINT__POINTID:
        return pointid != null;
    }
    return super.eIsSet(featureID);
  }

} //CheckPointImpl
