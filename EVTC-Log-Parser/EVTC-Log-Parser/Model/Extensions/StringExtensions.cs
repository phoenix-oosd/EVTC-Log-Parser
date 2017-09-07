using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EVTC_Log_Parser.Model.Extensions
{
    public static class StringExtensions
    {
        #region Extensions
        public static string SurroundWith(this string str, string value)
        {
            return value + str + value;
        }
        #endregion
    }
}
