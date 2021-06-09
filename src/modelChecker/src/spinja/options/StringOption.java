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

package spinja.options;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class StringOption extends Option implements Iterable<String> {
	private String value = null;
	private List<String> values;
	private boolean allowMultiple;

	public StringOption(final char letter, final String description,
						boolean allowMultiple) {
	    super(letter, allowMultiple ? description +" (multiple allowed)" : description);
		value = null;
		this.allowMultiple = allowMultiple;
		if (allowMultiple) {
			values = new LinkedList<String>();
		}
	}

	public String getValue() {
		if (allowMultiple)
			throw new AssertionError("wrong use, allowMultiple on, iterate this class");
		return value;
	}

	@Override
	public boolean isSet() {
		if (allowMultiple) {
			return !values.isEmpty();
		} else {
			return value != null;
		}
	}

	@Override
	public void parseOption(final String rest) {
		if (allowMultiple) {
			values.add(rest);
		} else {
			if (value != null)
				throw new AssertionError("Only one -"+ letter +" option allowed.");
			value = rest;
		}
	}

	@Override
	public String toString() {
		return super.toString("[text]");
	}

    @Override
    public Iterator<String> iterator() {
        if (!allowMultiple)
            throw new AssertionError("wrong use, allowMultiple off. Use getValue");
        return values.iterator();
    }
}
