using System;

namespace EVTC_Log_Parser.Model
{
    public class Metadata
    {
        #region Properties
        public string ArcVersion { get; set; }
        public int TargetSpeciesId { get; set; }
        public DateTime LogStart { get; set; }
        public DateTime LogEnd { get; set; }
        public string PointOfView { get; set; }
        public Language Language { get; set; }
        public int GWBuild { get; set; }
        public int ShardID { get; set; }
        #endregion
    }
}
