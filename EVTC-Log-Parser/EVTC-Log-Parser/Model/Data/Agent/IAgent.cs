using System;

namespace EVTC_Log_Parser.Model
{
    public interface IAgent
    {
        #region Properties
        string Address { get; set; }
        int FirstAware { get; set; }
        int LastAware { get; set; }
        int Instid { get; set; }
        #endregion
    }
}
