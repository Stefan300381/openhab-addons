/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.rfxcom.internal.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openhab.binding.rfxcom.internal.RFXComBindingConstants.*;
import static org.openhab.binding.rfxcom.internal.messages.RFXComBaseMessage.PacketType.LIGHTING5;
import static org.openhab.binding.rfxcom.internal.messages.RFXComLighting5Message.Commands.*;
import static org.openhab.binding.rfxcom.internal.messages.RFXComLighting5Message.SubType.IT;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.Test;
import org.openhab.binding.rfxcom.internal.exceptions.RFXComException;
import org.openhab.binding.rfxcom.internal.exceptions.RFXComUnsupportedChannelException;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.util.HexUtils;

/**
 * Test for RFXCom-binding
 *
 * @author Martin van Wingerden - Initial contribution
 */
@NonNullByDefault
public class RFXComLighting5MessageTest {
    private final MockDeviceState deviceState = new MockDeviceState();

    @Test
    public void convertFromStateItMessage() throws RFXComException {
        RFXComDeviceMessage itMessageObject = (RFXComDeviceMessage) RFXComMessageFactory.createMessage(LIGHTING5);
        itMessageObject.setDeviceId("2061.1");
        itMessageObject.setSubType(IT);
        itMessageObject.convertFromState(CHANNEL_COMMAND, OnOffType.ON);
        byte[] message = itMessageObject.decodeMessage();
        String hexMessage = HexUtils.bytesToHex(message);
        assertEquals("0A140F0000080D01010000", hexMessage, "Message is not as expected");
        RFXComLighting5Message msg = (RFXComLighting5Message) RFXComMessageFactory.createMessage(message);
        assertEquals(IT, msg.subType, "SubType");
        assertEquals("2061.1", msg.getDeviceId(), "Sensor Id");
        assertEquals(ON, msg.command, "Command");
    }

    @Test
    public void basicBoundaryCheck() throws RFXComException {
        RFXComLighting5Message message = (RFXComLighting5Message) RFXComMessageFactory.createMessage(LIGHTING5);

        message.subType = RFXComLighting5Message.SubType.LIGHTWAVERF;
        message.command = ON;

        RFXComTestHelper.basicBoundaryCheck(LIGHTING5, message);
    }

    // TODO please add more tests for different messages

    @Test
    public void testStringCommandOpenRelay() throws RFXComUnsupportedChannelException {
        RFXComLighting5Message msg = new RFXComLighting5Message();

        msg.convertFromState(CHANNEL_COMMAND_STRING, StringType.valueOf("OPEN_RELAY"));
        assertEquals(StringType.valueOf("OPEN_RELAY"), msg.convertToState(CHANNEL_COMMAND_STRING, deviceState));
        assertEquals(OPEN_RELAY, msg.command);
    }

    @Test
    public void testStringCommandCloseRelay() throws RFXComUnsupportedChannelException {
        RFXComLighting5Message msg = new RFXComLighting5Message();

        msg.convertFromState(CHANNEL_COMMAND_STRING, StringType.valueOf("CLOSE_RELAY"));
        assertEquals(StringType.valueOf("CLOSE_RELAY"), msg.convertToState(CHANNEL_COMMAND_STRING, deviceState));
        assertEquals(CLOSE_RELAY, msg.command);
    }

    @Test
    public void testStringCommandStopRelay() throws RFXComUnsupportedChannelException {
        RFXComLighting5Message msg = new RFXComLighting5Message();

        msg.convertFromState(CHANNEL_COMMAND_STRING, StringType.valueOf("STOP_RELAY"));
        assertEquals(StringType.valueOf("STOP_RELAY"), msg.convertToState(CHANNEL_COMMAND_STRING, deviceState));
        assertEquals(STOP_RELAY, msg.command);
    }
}
