/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import scheduling.dsl.DslPackage;
import scheduling.dsl.ParameterList;
import scheduling.dsl.ProcessDef;
import scheduling.dsl.PropertyAssignment;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Process Def</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.impl.ProcessDefImpl#getProctype <em>Proctype</em>}</li>
 *   <li>{@link scheduling.dsl.impl.ProcessDefImpl#getParalist <em>Paralist</em>}</li>
 *   <li>{@link scheduling.dsl.impl.ProcessDefImpl#getPropertyassignment <em>Propertyassignment</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProcessDefImpl extends MinimalEObjectImpl.Container implements ProcessDef
{
  /**
   * The cached value of the '{@link #getProctype() <em>Proctype</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProctype()
   * @generated
   * @ordered
   */
  protected scheduling.dsl.Process proctype;

  /**
   * The cached value of the '{@link #getParalist() <em>Paralist</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParalist()
   * @generated
   * @ordered
   */
  protected ParameterList paralist;

  /**
   * The cached value of the '{@link #getPropertyassignment() <em>Propertyassignment</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPropertyassignment()
   * @generated
   * @ordered
   */
  protected EList<PropertyAssignment> propertyassignment;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProcessDefImpl()
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
    return DslPackage.eINSTANCE.getProcessDef();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public scheduling.dsl.Process getProctype()
  {
    return proctype;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetProctype(scheduling.dsl.Process newProctype, NotificationChain msgs)
  {
    scheduling.dsl.Process oldProctype = proctype;
    proctype = newProctype;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.PROCESS_DEF__PROCTYPE, oldProctype, newProctype);
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
  public void setProctype(scheduling.dsl.Process newProctype)
  {
    if (newProctype != proctype)
    {
      NotificationChain msgs = null;
      if (proctype != null)
        msgs = ((InternalEObject)proctype).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.PROCESS_DEF__PROCTYPE, null, msgs);
      if (newProctype != null)
        msgs = ((InternalEObject)newProctype).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.PROCESS_DEF__PROCTYPE, null, msgs);
      msgs = basicSetProctype(newProctype, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.PROCESS_DEF__PROCTYPE, newProctype, newProctype));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ParameterList getParalist()
  {
    return paralist;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetParalist(ParameterList newParalist, NotificationChain msgs)
  {
    ParameterList oldParalist = paralist;
    paralist = newParalist;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.PROCESS_DEF__PARALIST, oldParalist, newParalist);
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
  public void setParalist(ParameterList newParalist)
  {
    if (newParalist != paralist)
    {
      NotificationChain msgs = null;
      if (paralist != null)
        msgs = ((InternalEObject)paralist).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.PROCESS_DEF__PARALIST, null, msgs);
      if (newParalist != null)
        msgs = ((InternalEObject)newParalist).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.PROCESS_DEF__PARALIST, null, msgs);
      msgs = basicSetParalist(newParalist, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.PROCESS_DEF__PARALIST, newParalist, newParalist));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<PropertyAssignment> getPropertyassignment()
  {
    if (propertyassignment == null)
    {
      propertyassignment = new EObjectContainmentEList<PropertyAssignment>(PropertyAssignment.class, this, DslPackage.PROCESS_DEF__PROPERTYASSIGNMENT);
    }
    return propertyassignment;
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
      case DslPackage.PROCESS_DEF__PROCTYPE:
        return basicSetProctype(null, msgs);
      case DslPackage.PROCESS_DEF__PARALIST:
        return basicSetParalist(null, msgs);
      case DslPackage.PROCESS_DEF__PROPERTYASSIGNMENT:
        return ((InternalEList<?>)getPropertyassignment()).basicRemove(otherEnd, msgs);
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
      case DslPackage.PROCESS_DEF__PROCTYPE:
        return getProctype();
      case DslPackage.PROCESS_DEF__PARALIST:
        return getParalist();
      case DslPackage.PROCESS_DEF__PROPERTYASSIGNMENT:
        return getPropertyassignment();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case DslPackage.PROCESS_DEF__PROCTYPE:
        setProctype((scheduling.dsl.Process)newValue);
        return;
      case DslPackage.PROCESS_DEF__PARALIST:
        setParalist((ParameterList)newValue);
        return;
      case DslPackage.PROCESS_DEF__PROPERTYASSIGNMENT:
        getPropertyassignment().clear();
        getPropertyassignment().addAll((Collection<? extends PropertyAssignment>)newValue);
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
      case DslPackage.PROCESS_DEF__PROCTYPE:
        setProctype((scheduling.dsl.Process)null);
        return;
      case DslPackage.PROCESS_DEF__PARALIST:
        setParalist((ParameterList)null);
        return;
      case DslPackage.PROCESS_DEF__PROPERTYASSIGNMENT:
        getPropertyassignment().clear();
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
      case DslPackage.PROCESS_DEF__PROCTYPE:
        return proctype != null;
      case DslPackage.PROCESS_DEF__PARALIST:
        return paralist != null;
      case DslPackage.PROCESS_DEF__PROPERTYASSIGNMENT:
        return propertyassignment != null && !propertyassignment.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //ProcessDefImpl
