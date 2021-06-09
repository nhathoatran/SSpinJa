// Copyright 2010, University of Twente, Formal Methods and Tools group
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package spinja.util;

public interface DataReader {

	/**
	 * Returns the place where the mark is currently placed. There is no guarantee what kind of
	 * numbers this will return, but when reading an number of bytes, the mark must be increased by
	 * that same number.
	 * @return The place where the mark is currently placed.
	 */
	public int getMark();

	/**
	 * Remembers the current place in the DataReader, so that it can be returned to with the
	 * {@link #resetMark()} method.
	 */
	public void storeMark();

	/**
	 * Resets the mark to the place where the {@link #storeMark()} was called. When this was never
	 * called it is reset to the beginning of the data.
	 */
	public void resetMark();

	/**
	 * Reads a boolean value from the storage and moves the pointer to the next place.
	 * @return The boolean value.
	 */
	public int readBoolean();
	
	/*
	 * Read a boolean value and return boolean value
	 */
	public boolean readBool() ;

	/**
	 * Reads a boolean value from the storage, but keeps the pointer in the same place.
	 * @return The boolean value.
	 */
	public int peekBoolean();

	/**
	 * Reads a byte value from the storage and moves the pointer 8 bits ahead.
	 * @return The byte value.
	 */
	public int readByte();

	/**
	 * Reads a byte value from the storage, but keeps the pointer in the same place.
	 * @return The byte value.
	 */
	public int peekByte();

	/**
	 * Reads a short value from the storage and moves the pointer 16 bits ahead.
	 * @return The short value.
	 */
	public int readShort();

	/**
	 * Reads a short value from the storage, but keeps the pointer in the same place.
	 * @return The short value.
	 */
	public int peekShort();

	/**
	 * Reads a integer value from the storage and moves the pointer 32 bits ahead.
	 * @return The integer value.
	 */
	public int readInt();

	/**
	 * Reads a integer value from the storage, but keeps the pointer in the same place.
	 * @return The integer value.
	 */
	public int peekInt();
	
	public int getSize() ;
}
