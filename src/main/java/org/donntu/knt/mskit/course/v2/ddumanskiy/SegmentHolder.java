package org.donntu.knt.mskit.course.v2.ddumanskiy;


import org.donntu.knt.mskit.course.v2.ddumanskiy.segments.DHTSegment;
import org.donntu.knt.mskit.course.v2.ddumanskiy.segments.DQTSegment;
import org.donntu.knt.mskit.course.v2.ddumanskiy.segments.SOFSegment;
import org.donntu.knt.mskit.course.v2.ddumanskiy.segments.SOSSegment;

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
