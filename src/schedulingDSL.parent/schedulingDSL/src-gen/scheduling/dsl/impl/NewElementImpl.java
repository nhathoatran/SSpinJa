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
import scheduling.dsl.NewElement;
import scheduling.dsl.ParaValue;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>New Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.impl.NewElementImpl#getProcess <em>Process</em>}</li>
 *   <li>{@link scheduling.dsl.impl.NewElementImpl#getParaAssign <em>Para Assign</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NewElementImpl extends MinimalEObjectImpl.Container implements NewElement
{
  /**
   * The cached value of the '{@link #getProcess() <em>Process</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProcess()
   * @generated
   * @ordered
   */
  protected scheduling.dsl.Process process;

  /**
   * The cached value of the '{@link #getParaAssign() <em>Para Assign</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParaAssign()
   * @generated
   * @ordered
   */
  protected EList<ParaValue> paraAssign;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected NewElementImpl()
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
    return DslPackage.eINSTANCE.getNewElement();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public scheduling.dsl.Process getProcess()
  {
    return process;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetProcess(scheduling.dsl.Process newProcess, NotificationChain msgs)
  {
    scheduling.dsl.Process oldProcess = process;
    process = newProcess;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.NEW_ELEMENT__PROCESS, oldProcess, newProcess);
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
  public void setProcess(scheduling.dsl.Process newProcess)
  {
    if (newProcess != process)
    {
      NotificationChain msgs = null;
      if (process != null)
        msgs = ((InternalEObject)process).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.NEW_ELEMENT__PROCESS, null, msgs);
      if (newProcess != null)
        msgs = ((InternalEObject)newProcess).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.NEW_ELEMENT__PROCESS, null, msgs);
      msgs = basicSetProcess(newProcess, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.NEW_ELEMENT__PROCESS, newProcess, newProcess));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<ParaValue> getParaAssign()
  {
    if (paraAssign == null)
    {
      paraAssign = new EObjectContainmentEList<ParaValue>(ParaValue.class, this, DslPackage.NEW_ELEMENT__PARA_ASSIGN);
    }
    return paraAssign;
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
      case DslPackage.NEW_ELEMENT__PROCESS:
        return basicSetProcess(null, msgs);
      case DslPackage.NEW_ELEMENT__PARA_ASSIGN:
        return ((InternalEList<?>)getParaAssign()).basicRemove(otherEnd, msgs);
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
      case DslPackage.NEW_ELEMENT__PROCESS:
        return getProcess();
      case DslPackage.NEW_ELEMENT__PARA_ASSIGN:
        return getParaAssign();
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
      case DslPackage.NEW_ELEMENT__PROCESS:
        setProcess((scheduling.dsl.Process)newValue);
        return;
      case DslPackage.NEW_ELEMENT__PARA_ASSIGN:
        getParaAssign().clear();
        getParaAssign().addAll((Collection<? extends ParaValue>)newValue);
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
      case DslPackage.NEW_ELEMENT__PROCESS:
        setProcess((scheduling.dsl.Process)null);
        return;
      case DslPackage.NEW_ELEMENT__PARA_ASSIGN:
        getParaAssign().clear();
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
      case DslPackage.NEW_ELEMENT__PROCESS:
        return process != null;
      case DslPackage.NEW_ELEMENT__PARA_ASSIGN:
        return paraAssign != null && !paraAssign.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //NewElementImpl
