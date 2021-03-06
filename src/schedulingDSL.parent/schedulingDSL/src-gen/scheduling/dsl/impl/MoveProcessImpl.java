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
import scheduling.dsl.MoveProcess;
import scheduling.dsl.SchedulerSet;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Move Process</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.impl.MoveProcessImpl#getProcess <em>Process</em>}</li>
 *   <li>{@link scheduling.dsl.impl.MoveProcessImpl#getColTo <em>Col To</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MoveProcessImpl extends StatementImpl implements MoveProcess
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
   * The cached value of the '{@link #getColTo() <em>Col To</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getColTo()
   * @generated
   * @ordered
   */
  protected SchedulerSet colTo;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MoveProcessImpl()
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
    return DslPackage.eINSTANCE.getMoveProcess();
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.MOVE_PROCESS__PROCESS, oldProcess, newProcess);
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
        msgs = ((InternalEObject)process).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.MOVE_PROCESS__PROCESS, null, msgs);
      if (newProcess != null)
        msgs = ((InternalEObject)newProcess).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.MOVE_PROCESS__PROCESS, null, msgs);
      msgs = basicSetProcess(newProcess, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.MOVE_PROCESS__PROCESS, newProcess, newProcess));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SchedulerSet getColTo()
  {
    return colTo;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetColTo(SchedulerSet newColTo, NotificationChain msgs)
  {
    SchedulerSet oldColTo = colTo;
    colTo = newColTo;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.MOVE_PROCESS__COL_TO, oldColTo, newColTo);
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
  public void setColTo(SchedulerSet newColTo)
  {
    if (newColTo != colTo)
    {
      NotificationChain msgs = null;
      if (colTo != null)
        msgs = ((InternalEObject)colTo).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.MOVE_PROCESS__COL_TO, null, msgs);
      if (newColTo != null)
        msgs = ((InternalEObject)newColTo).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.MOVE_PROCESS__COL_TO, null, msgs);
      msgs = basicSetColTo(newColTo, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.MOVE_PROCESS__COL_TO, newColTo, newColTo));
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
      case DslPackage.MOVE_PROCESS__PROCESS:
        return basicSetProcess(null, msgs);
      case DslPackage.MOVE_PROCESS__COL_TO:
        return basicSetColTo(null, msgs);
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
      case DslPackage.MOVE_PROCESS__PROCESS:
        return getProcess();
      case DslPackage.MOVE_PROCESS__COL_TO:
        return getColTo();
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
      case DslPackage.MOVE_PROCESS__PROCESS:
        setProcess((scheduling.dsl.Process)newValue);
        return;
      case DslPackage.MOVE_PROCESS__COL_TO:
        setColTo((SchedulerSet)newValue);
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
      case DslPackage.MOVE_PROCESS__PROCESS:
        setProcess((scheduling.dsl.Process)null);
        return;
      case DslPackage.MOVE_PROCESS__COL_TO:
        setColTo((SchedulerSet)null);
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
      case DslPackage.MOVE_PROCESS__PROCESS:
        return process != null;
      case DslPackage.MOVE_PROCESS__COL_TO:
        return colTo != null;
    }
    return super.eIsSet(featureID);
  }

} //MoveProcessImpl
