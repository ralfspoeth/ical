package biweekly.util;

import static biweekly.util.TestUtils.buildTimezone;

import java.util.TimeZone;

import org.junit.rules.ExternalResource;

/*
 Copyright (c) 2013-2023, Michael Angstadt
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met: 

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer. 
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution. 

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * Changes the JVM's default timezone temporarily for all of the tests in a test
 * class.
 * @author Michael Angstadt
 */
public class DefaultTimezoneRule extends ExternalResource {
	private final TimeZone newTz;
	private TimeZone defaultTz;

	/**
	 * @param hour the hour component of the UTC offset
	 * @param minute the minute component of the UTC offset
	 */
	public DefaultTimezoneRule(int hour, int minute) {
		newTz = buildTimezone(hour, minute);
	}
	
	/**
	 * @param tzid the timezone ID (e.g. "America/New_York")
	 */
	public DefaultTimezoneRule(String tzid) {
		newTz = TimeZone.getTimeZone(tzid);
	}

	@Override
	protected void before() {
		defaultTz = TimeZone.getDefault();
		TimeZone.setDefault(newTz);
	}

	@Override
	protected void after() {
		TimeZone.setDefault(defaultTz);
	}
}
