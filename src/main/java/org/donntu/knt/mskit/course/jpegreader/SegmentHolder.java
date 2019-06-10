package org.donntu.knt.mskit.course.jpegreader;


import org.donntu.knt.mskit.course.jpegreader.segments.DQTSegment;
import org.donntu.knt.mskit.course.jpegreader.segments.DHTSegment;
import org.donntu.knt.mskit.course.jpegreader.segments.SOFSegment;
import org.donntu.knt.mskit.course.jpegreader.segments.SOSSegment;

/**
 * Just a holder for all read JPEG headers.
 *
 * User: ddumanskiy
 * Date: 8/13/2014
 * Time: 9:36 AM
 */
public class SegmentHolder {

    SOFSegment sofSegment = null;
    SOSSegment sosSegment = null;
    DHTSegment dhtSegment = new DHTSegment();
    DQTSegment dqtSegment = new DQTSegment();

}
