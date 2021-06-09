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
import scheduling.dsl.GetName;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Name</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.impl.GetNameImpl#getProc <em>Proc</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GetNameImpl extends ExpressionImpl implements GetName
{
  /**
   * The cached value of the '{@link #getProc() <em>Proc</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProc()
   * @generated
   * @ordered
   */
  protected scheduling.dsl.Process proc;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected GetNameImpl()
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
    return DslPackage.eINSTANCE.getGetName();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public scheduling.dsl.Process getProc()
  {
    return proc;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetProc(scheduling.dsl.Process newProc, NotificationChain msgs)
  {
    scheduling.dsl.Process oldProc = proc;
    proc = newProc;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.GET_NAME__PROC, oldProc, newProc);
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
  public void setProc(scheduling.dsl.Process newProc)
  {
    if (newProc != proc)
    {
      NotificationChain msgs = null;
      if (proc != null)
        msgs = ((InternalEObject)proc).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.GET_NAME__PROC, null, msgs);
      if (newProc != null)
        msgs = ((InternalEObject)newProc).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.GET_NAME__PROC, null, msgs);
      msgs = basicSetProc(newProc, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.GET_NAME__PROC, newProc, newProc));
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
      case DslPackage.GET_NAME__PROC:
        return basicSetProc(null, msgs);
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
      case DslPackage.GET_NAME__PROC:
        return getProc();
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
      case DslPackage.GET_NAME__PROC:
        setProc((scheduling.dsl.Process)newValue);
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
      case DslPackage.GET_NAME__PROC:
        setProc((scheduling.dsl.Process)null);
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
      case DslPackage.GET_NAME__PROC:
        return proc != null;
    }
    return super.eIsSet(featureID);
  }

} //GetNameImpl
