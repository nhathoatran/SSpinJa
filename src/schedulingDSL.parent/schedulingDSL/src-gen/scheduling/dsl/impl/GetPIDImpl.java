/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scheduling.dsl.DslPackage;
import scheduling.dsl.GetPID;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get PID</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.impl.GetPIDImpl#getProcName <em>Proc Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GetPIDImpl extends AtomicImpl implements GetPID
{
  /**
   * The cached value of the '{@link #getProcName() <em>Proc Name</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProcName()
   * @generated
   * @ordered
   */
  protected scheduling.dsl.Process procName;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected GetPIDImpl()
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
    return DslPackage.eINSTANCE.getGetPID();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public scheduling.dsl.Process getProcName()
  {
    return procName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetProcName(scheduling.dsl.Process newProcName, NotificationChain msgs)
  {
    scheduling.dsl.Process oldProcName = procName;
    procName = newProcName;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.GET_PID__PROC_NAME, oldProcName, newProcName);
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
  public void setProcName(scheduling.dsl.Process newProcName)
  {
    if (newProcName != procName)
    {
      NotificationChain msgs = null;
      if (procName != null)
        msgs = ((InternalEObject)procName).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.GET_PID__PROC_NAME, null, msgs);
      if (newProcName != null)
        msgs = ((InternalEObject)newProcName).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.GET_PID__PROC_NAME, null, msgs);
      msgs = basicSetProcName(newProcName, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.GET_PID__PROC_NAME, newProcName, newProcName));
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
      case DslPackage.GET_PID__PROC_NAME:
        return basicSetProcName(null, msgs);
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
      case DslPackage.GET_PID__PROC_NAME:
        return getProcName();
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
      case DslPackage.GET_PID__PROC_NAME:
        setProcName((scheduling.dsl.Process)newValue);
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
      case DslPackage.GET_PID__PROC_NAME:
        setProcName((scheduling.dsl.Process)null);
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
      case DslPackage.GET_PID__PROC_NAME:
        return procName != null;
    }
    return super.eIsSet(featureID);
  }

} //GetPIDImpl
