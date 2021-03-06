/**
 */
package com.specmate.model.history;

import org.eclipse.emf.cdo.CDOObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.specmate.model.history.Change#getNewValue <em>New Value</em>}</li>
 *   <li>{@link com.specmate.model.history.Change#getFeature <em>Feature</em>}</li>
 *   <li>{@link com.specmate.model.history.Change#isIsCreate <em>Is Create</em>}</li>
 *   <li>{@link com.specmate.model.history.Change#isIsDelete <em>Is Delete</em>}</li>
 * </ul>
 *
 * @see com.specmate.model.history.HistoryPackage#getChange()
 * @model
 * @extends CDOObject
 * @generated
 */
public interface Change extends CDOObject {
	/**
	 * Returns the value of the '<em><b>New Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Value</em>' attribute.
	 * @see #setNewValue(String)
	 * @see com.specmate.model.history.HistoryPackage#getChange_NewValue()
	 * @model
	 * @generated
	 */
	String getNewValue();

	/**
	 * Sets the value of the '{@link com.specmate.model.history.Change#getNewValue <em>New Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Value</em>' attribute.
	 * @see #getNewValue()
	 * @generated
	 */
	void setNewValue(String value);

	/**
	 * Returns the value of the '<em><b>Feature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature</em>' attribute.
	 * @see #setFeature(String)
	 * @see com.specmate.model.history.HistoryPackage#getChange_Feature()
	 * @model
	 * @generated
	 */
	String getFeature();

	/**
	 * Sets the value of the '{@link com.specmate.model.history.Change#getFeature <em>Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Feature</em>' attribute.
	 * @see #getFeature()
	 * @generated
	 */
	void setFeature(String value);

	/**
	 * Returns the value of the '<em><b>Is Create</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Create</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Create</em>' attribute.
	 * @see #setIsCreate(boolean)
	 * @see com.specmate.model.history.HistoryPackage#getChange_IsCreate()
	 * @model
	 * @generated
	 */
	boolean isIsCreate();

	/**
	 * Sets the value of the '{@link com.specmate.model.history.Change#isIsCreate <em>Is Create</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Create</em>' attribute.
	 * @see #isIsCreate()
	 * @generated
	 */
	void setIsCreate(boolean value);

	/**
	 * Returns the value of the '<em><b>Is Delete</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Delete</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Delete</em>' attribute.
	 * @see #setIsDelete(boolean)
	 * @see com.specmate.model.history.HistoryPackage#getChange_IsDelete()
	 * @model
	 * @generated
	 */
	boolean isIsDelete();

	/**
	 * Sets the value of the '{@link com.specmate.model.history.Change#isIsDelete <em>Is Delete</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Delete</em>' attribute.
	 * @see #isIsDelete()
	 * @generated
	 */
	void setIsDelete(boolean value);

} // Change
